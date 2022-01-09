package com.service;

import com.context.Lend;
import com.context.LendKey;
import com.repository.LendRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LendService {
    private final LendRepository repository;

    public LendService(LendRepository repository) {
        this.repository = repository;
    }

    public List<Lend> getLends() {
        return repository.findAll();
    }

    public List<Lend> getLendsSortedByLendTime(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "lend_time"));
    }

    public List<Lend> getLendsSortedByReturnTime(Sort.Direction direction) {
        return repository.findAll(Sort.by(direction, "return_time"));
    }

    public List<Lend> getLendsBefore(LocalDateTime lendTime) {
        return repository.findAllByLendTimeBefore(lendTime);
    }

    public List<Lend> getLendsAfter(LocalDateTime lendTime) {
        return repository.findAllByLendTimeAfter(lendTime);
    }

    public List<Lend> getLendsReturnedBefore(LocalDateTime returnTime) {
        return repository.findAllByReturnTimeBefore(returnTime);
    }

    public List<Lend> getLendsReturnedAfter(LocalDateTime returnTime) {
        return repository.findAllByReturnTimeAfter(returnTime);
    }

    public List<Lend> getLendsByStatus(Lend.LendStatus lendStatus) {
        return repository.findAllByLendStatus(lendStatus);
    }

    public Optional<Lend> getLendById(LendKey id) {
        return repository.findById(id);
    }

    public void saveLend(Lend lend) {
        repository.save(lend);
    }

    public void deleteAuthor(Lend lend) {
        repository.delete(lend);
    }
}
