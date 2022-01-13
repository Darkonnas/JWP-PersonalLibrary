package com.controller;

import com.context.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BookService;
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

@WebMvcTest(controllers = GenreController.class)
@EnableTransactionManagement
class GenreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private static Genre genre1;
    private static List<Genre> existingGenres;

    @BeforeAll
    public static void setup() {
        genre1 = new Genre();
        existingGenres = List.of(genre1);
    }

    @Test
    void getGenres() throws Exception {
        when(bookService.getGenres()).thenReturn(existingGenres).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/genres")).andExpect(status().isOk());
        mockMvc.perform(get("/api/genres")).andExpect(status().isNoContent());
    }

    @Test
    void getGenre() throws Exception {
        when(bookService.getGenreById(0L)).thenReturn(Optional.of(genre1));
        when(bookService.getGenreById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/genres/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/genres/1")).andExpect(status().isNotFound());
    }

    @Test
    void addGenre() throws Exception {
        mockMvc.perform(post("/api/genres/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(genre1))).andExpect(status().isCreated());
    }

    @Test
    void updateGenre() throws Exception {
        when(bookService.getGenreById(0L)).thenReturn(Optional.of(genre1));
        when(bookService.getGenreById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/genres/0").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(genre1))).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/genres/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(genre1))).andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre() throws Exception {
        when(bookService.getGenreById(0L)).thenReturn(Optional.of(genre1));
        when(bookService.getGenreById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/genres/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/genres/1")).andExpect(status().isNotFound());
    }
}