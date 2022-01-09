package com.service;

import com.context.Friend;
import com.repository.FriendRepository;
import org.springframework.data.domain.Sort;
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

    public List<Friend> getFriendsSortedByFirstName(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "first_name"));
    }

    public List<Friend> getFriendsSortedByLastName(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "last_name"));
    }

    public Optional<Friend> getFriendById(long id) {
        return repository.findById(id);
    }

    public void saveAuthor(Friend friend) {
        repository.save(friend);
    }

    public void deleteAuthor(Friend friend) {
        repository.delete(friend);
    }
}
