package com.service;

import com.context.Friend;
import com.repository.FriendRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    private final FriendRepository repository;

    public FriendService(FriendRepository repository) {
        this.repository = repository;
    }

    public List<Friend> getFriends() {
        return repository.findAll();
    }

    public Optional<Friend> getFriendById(Long id) {
        return repository.findById(id);
    }

    public void saveFriend(Friend friend) {
        repository.save(friend);
    }

    public void deleteFriend(Friend friend) {
        repository.delete(friend);
    }
}
