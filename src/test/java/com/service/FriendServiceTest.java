package com.service;

import com.context.Friend;
import com.repository.FriendRepository;
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
class FriendServiceTest {
    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRepository friendRepository;

    private static Friend existingFriend;
    private static List<Friend> nonEmptyResult;
    private static List<Friend> emptyResult;

    @BeforeAll
    public static void setup() {
        existingFriend = new Friend();
        nonEmptyResult = List.of(existingFriend);
        emptyResult = Collections.emptyList();
    }

    @Test
    void getFriends() {
        when(friendRepository.findAll()).thenReturn(nonEmptyResult).thenReturn(emptyResult);

        List<Friend> firstResult = friendService.getFriends();
        List<Friend> secondResult = friendService.getFriends();

        assertEquals(nonEmptyResult, firstResult);
        assertEquals(emptyResult, secondResult);
    }

    @Test
    void getFriendById() {
        when(friendRepository.findById(0L)).thenReturn(Optional.of(existingFriend));
        when(friendRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Friend> firstResult = friendService.getFriendById(0L);
        Optional<Friend> secondResult = friendService.getFriendById(1L);

        assertEquals(Optional.of(existingFriend), firstResult);
        assertEquals(Optional.empty(), secondResult);
    }
}