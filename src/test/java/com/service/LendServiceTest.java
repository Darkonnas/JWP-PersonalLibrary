package com.service;

import com.context.Book;
import com.context.BookCopy;
import com.context.Lend;
import com.context.LendExtension;
import com.repository.LendExtensionRepository;
import com.repository.LendRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LendServiceTest {
    @InjectMocks
    private LendService lendService;

    @Mock
    private LendRepository lendRepository;

    @Mock
    private LendExtensionRepository lendExtensionRepository;

    private static Lend existingLend;
    private static LendExtension lendExtension;
    private static List<Lend> nonEmptyResult;
    private static List<Lend> emptyResult;
    private static BookCopy bookCopy;


    @BeforeAll
    public static void setup()  {
        existingLend = new Lend();
        lendExtension = new LendExtension();
        nonEmptyResult = List.of(existingLend);
        emptyResult = Collections.emptyList();
        Book book = new Book();
        book.setTitle("Title");
        bookCopy = new BookCopy();
        bookCopy.setBook(book);
    }

    @Test
    void getLends() {
        when(lendRepository.findAll()).thenReturn(nonEmptyResult).thenReturn(emptyResult);

        List<Lend> firstResult = lendService.getLends();
        List<Lend> secondResult = lendService.getLends();

        assertEquals(firstResult, nonEmptyResult);
        assertEquals(secondResult, emptyResult);
    }

    @Test
    void getLendById() {
        when(lendRepository.findById(0L)).thenReturn(Optional.of(existingLend));
        when(lendRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Lend> firstResult = lendService.getLendById(0L);
        Optional<Lend> secondResult = lendService.getLendById(1L);

        assertEquals(Optional.of(existingLend), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getLendExtensionById() {
        when(lendExtensionRepository.findById(0L)).thenReturn(Optional.of(lendExtension));
        when(lendExtensionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<LendExtension> firstResult = lendService.getLendExtensionById(0L);
        Optional<LendExtension> secondResult = lendService.getLendExtensionById(1L);

        assertEquals(Optional.of(lendExtension), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }

    @Test
    void getLendsByFriend() {
    }

    @Test
    void getLastLendOfBookCopy() {
    }

    @Test
    void getLastExtensionForLend() {
    }

    @Test
    void isBookCurrentlyLend() {
    }

    @Test
    void getAvailableCopyForLending() {
    }

    @Test
    void approveLendExtension() {
    }

    @Test
    void canFriendBorrowBooks() {
    }

    @Test
    void canExtensionBeRequestedForLend() {
    }
}