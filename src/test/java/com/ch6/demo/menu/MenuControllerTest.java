package com.ch6.demo.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
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
}
