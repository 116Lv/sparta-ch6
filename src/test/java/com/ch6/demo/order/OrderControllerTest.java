package com.ch6.demo.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM order_outbox");
        jdbcTemplate.update("DELETE FROM orders");
        jdbcTemplate.update("DELETE FROM user_points");
        jdbcTemplate.update("DELETE FROM menus");
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (1, 'Latte', 4500, true)");
        jdbcTemplate.update("INSERT INTO user_points (id, user_id, balance) VALUES (1, 1, 10000)");
    }

    @Test
    void createOrderPaysWithUserPointsAndReturnsRemainingPoint() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "menuId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNumber())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.menuId").value(1))
                .andExpect(jsonPath("$.paymentAmount").value(4500))
                .andExpect(jsonPath("$.remainingPoint").value(5500))
                .andExpect(jsonPath("$.status").value("PAID"));

        Integer balance = jdbcTemplate.queryForObject(
                "SELECT balance FROM user_points WHERE user_id = 1",
                Integer.class
        );
        assertThat(balance).isEqualTo(5500);
    }

    @Test
    void createOrderSavesPendingOutboxEvent() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "menuId": 1
                                }
                                """))
                .andExpect(status().isOk());

        Integer outboxCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_outbox", Integer.class);
        String outboxStatus = jdbcTemplate.queryForObject("SELECT status FROM order_outbox", String.class);
        Integer retryCount = jdbcTemplate.queryForObject("SELECT retry_count FROM order_outbox", Integer.class);

        assertThat(outboxCount).isEqualTo(1);
        assertThat(outboxStatus).isEqualTo("PENDING");
        assertThat(retryCount).isZero();
    }

    @Test
    void createOrderRejectsInsufficientPointsAndDoesNotSaveOrder() throws Exception {
        jdbcTemplate.update("UPDATE user_points SET balance = 3000 WHERE user_id = 1");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "menuId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());

        Integer balance = jdbcTemplate.queryForObject(
                "SELECT balance FROM user_points WHERE user_id = 1",
                Integer.class
        );
        Integer orderCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        Integer outboxCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_outbox", Integer.class);

        assertThat(balance).isEqualTo(3000);
        assertThat(orderCount).isZero();
        assertThat(outboxCount).isZero();
    }

    @Test
    void createOrderRejectsMissingMenuAndDoesNotSaveOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "menuId": 999
                                }
                                """))
                .andExpect(status().isNotFound());

        Integer orderCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        Integer outboxCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_outbox", Integer.class);

        assertThat(orderCount).isZero();
        assertThat(outboxCount).isZero();
    }
}
