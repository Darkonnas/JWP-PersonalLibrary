package com.controller;

import com.context.Book;
import com.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryManagementController.class)
@EnableTransactionManagement
class LibraryManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private static Map<Integer, Map<Integer, Map<Integer, List<Book>>>> layout;

    @BeforeAll
    public static void setup() {
        layout = Map.of(1, Map.of(1, Map.of(1, List.of(new Book()))));
    }

    @Test
    void getLibraryBookCopiesLayout() throws Exception {
        when(bookService.getLibraryBookCopiesLayout()).thenReturn(layout).thenReturn(new HashMap<>());
        mockMvc.perform(get("/api/library-management/layout")).andExpect(status().isOk());
        mockMvc.perform(get("/api/library-management/layout")).andExpect(status().isNoContent());
    }
}