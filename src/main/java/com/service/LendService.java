package com.service;

import com.context.Lend;
import com.repository.LendExtensionRepository;
import com.repository.LendRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Lend> getLendsSortedByLendTime(Sort.Direction direction) {
        return lendRepository.findAll(Sort.by(direction, "lend_time"));
    }

    public List<Lend> getLendsSortedByReturnTime(Sort.Direction direction) {
        return lendRepository.findAll(Sort.by(direction, "return_time"));
    }

    public List<Lend> getLendsBefore(LocalDateTime lendTime) {
        return lendRepository.findAllByLendTimeBefore(lendTime);
    }

    public List<Lend> getLendsAfter(LocalDateTime lendTime) {
        return lendRepository.findAllByLendTimeAfter(lendTime);
    }

    public List<Lend> getLendsReturnedBefore(LocalDateTime returnTime) {
        return lendRepository.findAllByReturnTimeBefore(returnTime);
    }

    public List<Lend> getLendsReturnedAfter(LocalDateTime returnTime) {
        return lendRepository.findAllByReturnTimeAfter(returnTime);
    }

    public List<Lend> getLendsByStatus(Lend.LendStatus lendStatus) {
        return lendRepository.findAllByLendStatus(lendStatus);
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
