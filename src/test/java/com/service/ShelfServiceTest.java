package com.service;

import com.context.Book;
import com.context.BookCopy;
import com.context.Shelf;
import com.repository.ShelfRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {
    @InjectMocks
    private ShelfService shelfService;

    @Mock
    private ShelfRepository shelfRepository;

    private static Shelf existingShelf;
    private static List<Shelf> nonEmptyResult;
    private static List<Shelf> emptyResult;
    private static BookCopy bookCopy;

    @BeforeAll
    public static void setup()  {
        existingShelf = new Shelf();
        nonEmptyResult = List.of(existingShelf);
        emptyResult = Collections.emptyList();
        Book book = new Book();
        book.setTitle("Title");
        bookCopy = new BookCopy();
        bookCopy.setBook(book);
    }

    @Test
    void getShelves() {
        when(shelfRepository.findAll()).thenReturn(nonEmptyResult).thenReturn(emptyResult);

        List<Shelf> firstResult = shelfService.getShelves();
        List<Shelf> secondResult = shelfService.getShelves();

        assertEquals(nonEmptyResult, firstResult);
        assertEquals(emptyResult, secondResult);
    }

    @Test
    void getShelfById() {
        when(shelfRepository.findById(0L)).thenReturn(Optional.of(existingShelf));
        when(shelfRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Shelf> firstResult = shelfService.getShelfById(0L);
        Optional<Shelf> secondResult = shelfService.getShelfById(1L);

        assertEquals(Optional.of(existingShelf), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void canShelfStoreBookCopy() {
        assertTrue(shelfService.canShelfStoreBookCopy(existingShelf, bookCopy));
        existingShelf.setBookCopies(Collections.emptyList());
        existingShelf.setCapacity(0);
        assertFalse(shelfService.canShelfStoreBookCopy(existingShelf, bookCopy));
        existingShelf.setCapacity(1);
        existingShelf.setStartingLetter('T');
        assertTrue(shelfService.canShelfStoreBookCopy(existingShelf, bookCopy));
        existingShelf.setStartingLetter('F');
        assertFalse(shelfService.canShelfStoreBookCopy(existingShelf, bookCopy));
    }
}