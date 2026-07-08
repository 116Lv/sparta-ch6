package com.ch6.demo.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class MenuSeedDataTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMenusReturnsSeededCoffeeMenus() throws Exception {
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menus.length()").value(24))
                .andExpect(jsonPath("$.menus[0].name").value("에스프레소"))
                .andExpect(jsonPath("$.menus[0].price").value(1500))
                .andExpect(jsonPath("$.menus[1].name").value("아메리카노 HOT"))
                .andExpect(jsonPath("$.menus[1].price").value(1500))
                .andExpect(jsonPath("$.menus[23].name").value("디카페인 콜드브루 라떼"))
                .andExpect(jsonPath("$.menus[23].price").value(4000));
    }
}
