package com.controller;

import com.context.Friend;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.FriendService;
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

@WebMvcTest(controllers = FriendController.class)
@EnableTransactionManagement
class FriendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendService friendService;

    private static Friend friend1;
    private static List<Friend> existingFriends;

    @BeforeAll
    public static void setup() {
        friend1 = new Friend(0L, "Friend1_FirstName", "Friend1_LastName", "Friend1_Address");
        existingFriends = List.of(friend1);
    }

    @Test
    void getFriends() throws Exception {
        when(friendService.getFriends()).thenReturn(existingFriends).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/friends")).andExpect(status().isOk());
        mockMvc.perform(get("/api/friends")).andExpect(status().isNoContent());
    }

    @Test
    void getFriend() throws Exception {
        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friend1));
        when(friendService.getFriendById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/friends/0")).andExpect(status().isOk());
        mockMvc.perform(get("/api/friends/1")).andExpect(status().isNotFound());
    }

    @Test
    void addFriend() throws Exception {
        mockMvc.perform(post("/api/friends/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(friend1))).andExpect(status().isCreated());
    }

    @Test
    void updateFriend() throws Exception {
        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friend1));
        when(friendService.getFriendById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/friends/0").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(friend1))).andExpect(status().isNoContent());
        mockMvc.perform(put("/api/friends/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(friend1))).andExpect(status().isNotFound());
    }

    @Test
    void deleteFriend() throws Exception {
        when(friendService.getFriendById(0L)).thenReturn(Optional.of(friend1));
        when(friendService.getFriendById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/friends/0")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/friends/1")).andExpect(status().isNotFound());
    }
}