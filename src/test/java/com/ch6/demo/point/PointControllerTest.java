package com.ch6.demo.point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void chargePointsCreatesBalanceForUserWithoutPoints() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/points/charge", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 10000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.chargedAmount").value(10000))
                .andExpect(jsonPath("$.balance").value(10000));
    }

    @Test
    void chargePointsAddsAmountToExistingBalance() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/points/charge", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "amount": 10000
                        }
                        """));

        mockMvc.perform(post("/api/users/{userId}/points/charge", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 5000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.chargedAmount").value(5000))
                .andExpect(jsonPath("$.balance").value(15000));
    }

    @Test
    void chargePointsRejectsZeroAmount() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/points/charge", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
