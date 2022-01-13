package com.controller;

import com.context.LendExtension;
import com.service.LendService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LendExtensionController.class)
@EnableTransactionManagement
class LendExtensionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LendService lendService;

    private static LendExtension pendingLendExtension;
    private static LendExtension approvedLendExtension;

    @BeforeAll
    public static void setup() {
        pendingLendExtension = new LendExtension();
        approvedLendExtension = new LendExtension();
        pendingLendExtension.setExtensionStatus(LendExtension.LendExtensionStatus.PENDING);
        approvedLendExtension.setExtensionStatus(LendExtension.LendExtensionStatus.APPROVED);
    }

    @Test
    void respondToLendExtension() throws Exception {
        when(lendService.getLendExtensionById(0L)).thenReturn(Optional.of(pendingLendExtension));
        when(lendService.getLendExtensionById(1L)).thenReturn(Optional.of(approvedLendExtension));
        when(lendService.getLendExtensionById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/lend-extensions/0/respond").param("extensionDecision", "PENDING")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/lend-extensions/2/respond").param("extensionDecision", "APPROVED")).andExpect(status().isNotFound());
        mockMvc.perform(put("/api/lend-extensions/1/respond").param("extensionDecision", "APPROVED")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/lend-extensions/0/respond").param("extensionDecision", "APPROVED")).andExpect(status().isNoContent());
    }
}