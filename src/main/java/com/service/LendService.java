package com.service;

import com.context.Friend;
import com.context.Lend;
import com.repository.LendExtensionRepository;
import com.repository.LendRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LendService {
    private final LendRepository lendRepository;
    private final LendExtensionRepository lendExtensionRepository;

    public LendService(LendRepository repository, LendExtensionRepository lendExtensionRepository) {
        this.lendRepository = repository;
        this.lendExtensionRepository = lendExtensionRepository;
    }

    public List<Lend> getLends() {
        return lendRepository.findAll();
    }

    public List<Lend> getLendsByFriend(Friend friend) {
        return lendRepository.findAllByFriend(friend);
    }

    public Optional<Lend> getLendById(long id) {
        return lendRepository.findById(id);
    }

    public void saveLend(Lend lend) {
        lendRepository.save(lend);
    }

    public void deleteAuthor(Lend lend) {
        lendRepository.delete(lend);
    }
}
