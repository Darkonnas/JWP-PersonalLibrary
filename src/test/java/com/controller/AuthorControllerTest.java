package com.controller;

import com.context.Author;
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

@WebMvcTest(controllers = AuthorController.class)
@EnableTransactionManagement
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private static Author author1;
    private static List<Author> existingAuthors;

    @BeforeAll
    public static void setup() {
        author1 = new Author();
        existingAuthors = List.of(author1);
    }

    @Test
    void getAuthors() throws Exception {
        when(bookService.getAuthors()).thenReturn(existingAuthors).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/authors")).andExpect(status().isOk());
        mockMvc.perform(get("/api/authors")).andExpect(status().isNoContent());
    }

    @Test
    void getAuthor() throws Exception {
        when(bookService.getAuthorById(0L)).thenReturn(Optional.of(author1));
        when(bookService.getAuthorById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/authors/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/authors/1")).andExpect(status().isNotFound());
    }

    @Test
    void addAuthor() throws Exception {
        mockMvc.perform(post("/api/authors/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(author1))).andExpect(status().isCreated());
    }

    @Test
    void updateAuthor() throws Exception {
        when(bookService.getAuthorById(0L)).thenReturn(Optional.of(author1));
        when(bookService.getAuthorById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/authors/0").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(author1))).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/authors/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(author1))).andExpect(status().isNotFound());
    }

    @Test
    void deleteAuthor() throws Exception {
        when(bookService.getAuthorById(0L)).thenReturn(Optional.of(author1));
        when(bookService.getAuthorById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/authors/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/authors/1")).andExpect(status().isNotFound());
    }
}