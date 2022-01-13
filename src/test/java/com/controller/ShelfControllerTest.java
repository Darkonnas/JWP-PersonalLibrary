package com.controller;

import com.context.Shelf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.ShelfService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShelfController.class)
@EnableTransactionManagement
class ShelfControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShelfService bookService;

    private static Shelf shelf1;
    private static List<Shelf> existingShelves;

    @BeforeAll
    public static void setup() {
        shelf1 = new Shelf();
        existingShelves = List.of(shelf1);
    }

    @Test
    void getShelves() throws Exception {
        when(bookService.getShelves()).thenReturn(existingShelves).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/shelves")).andExpect(status().isOk());
        mockMvc.perform(get("/api/shelves")).andExpect(status().isNoContent());
    }

    @Test
    void getShelf() throws Exception {
        when(bookService.getShelfById(0L)).thenReturn(Optional.of(shelf1));
        when(bookService.getShelfById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/shelves/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/shelves/1")).andExpect(status().isNotFound());
    }

    @Test
    void addShelf() throws Exception {
        mockMvc.perform(post("/api/shelves/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(shelf1))).andExpect(status().isCreated());
    }

    @Test
    void updateShelf() throws Exception {
        when(bookService.getShelfById(0L)).thenReturn(Optional.of(shelf1));
        when(bookService.getShelfById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/shelves/0").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(shelf1))).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/shelves/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(shelf1))).andExpect(status().isNotFound());
    }

    @Test
    void deleteShelf() throws Exception {
        when(bookService.getShelfById(0L)).thenReturn(Optional.of(shelf1));
        when(bookService.getShelfById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/shelves/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/shelves/1")).andExpect(status().isNotFound());
    }
}