package com.ch6.demo.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM order_outbox");
        jdbcTemplate.update("DELETE FROM orders");
        jdbcTemplate.update("DELETE FROM menus");
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (1, 'Americano', 3000, true)");
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (2, 'Latte', 4500, true)");
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (3, 'Hidden Menu', 9999, false)");
    }

    @Test
    void getMenusReturnsActiveCoffeeMenuItems() throws Exception {
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menus.length()").value(2))
                .andExpect(jsonPath("$.menus[0].id").value(1))
                .andExpect(jsonPath("$.menus[0].name").value("Americano"))
                .andExpect(jsonPath("$.menus[0].price").value(3000))
                .andExpect(jsonPath("$.menus[1].id").value(2))
                .andExpect(jsonPath("$.menus[1].name").value("Latte"))
                .andExpect(jsonPath("$.menus[1].price").value(4500));
    }

    @Test
    void getPopularMenusReturnsTop3ByRecent7DaysOrderCount() throws Exception {
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (4, 'Mocha', 5000, true)");
        jdbcTemplate.update("INSERT INTO menus (id, name, price, active) VALUES (5, 'Tea', 3500, true)");
        insertRecentOrders(2, 3);
        insertRecentOrders(1, 2);
        insertRecentOrders(4, 1);
        insertRecentOrders(5, 1);
        insertOldOrders(5, 10);

        mockMvc.perform(get("/api/menus/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.popularMenus.length()").value(3))
                .andExpect(jsonPath("$.popularMenus[0].menuId").value(2))
                .andExpect(jsonPath("$.popularMenus[0].name").value("Latte"))
                .andExpect(jsonPath("$.popularMenus[0].price").value(4500))
                .andExpect(jsonPath("$.popularMenus[0].orderCount").value(3))
                .andExpect(jsonPath("$.popularMenus[1].menuId").value(1))
                .andExpect(jsonPath("$.popularMenus[1].orderCount").value(2))
                .andExpect(jsonPath("$.popularMenus[2].menuId").value(4))
                .andExpect(jsonPath("$.popularMenus[2].orderCount").value(1));
    }

    private void insertRecentOrders(long menuId, int count) {
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update("""
                    INSERT INTO orders (user_id, menu_id, payment_amount, status, ordered_at)
                    VALUES (?, ?, ?, 'PAID', DATEADD('DAY', -1, CURRENT_TIMESTAMP))
                    """, 1L, menuId, 1000);
        }
    }

    private void insertOldOrders(long menuId, int count) {
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update("""
                    INSERT INTO orders (user_id, menu_id, payment_amount, status, ordered_at)
                    VALUES (?, ?, ?, 'PAID', DATEADD('DAY', -8, CURRENT_TIMESTAMP))
                    """, 1L, menuId, 1000);
        }
    }
}
