package com.service;

import com.context.*;
import com.repository.LendExtensionRepository;
import com.repository.LendRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<Lend> getLendById(Long id) {
        return lendRepository.findById(id);
    }

    public Optional<LendExtension> getLendExtensionById (Long id) {
        return lendExtensionRepository.findById(id);
    }

    public List<Lend> getLendsByFriend(Friend friend) {
        return lendRepository.findAllByFriend(friend);
    }

    public Optional<Lend> getLastLendOfBookCopy(BookCopy bookCopy) {
        return bookCopy.getLends().stream().max(Comparator.comparing(Lend::getLendTime));
    }

    public Optional<LendExtension> getLastExtensionForLend(Lend lend) {
        return lend.getLendExtensions().stream().max(Comparator.comparing(LendExtension::getRequestedTime));
    }

    public boolean isBookCurrentlyLend(BookCopy bookCopy) {
        Optional<Lend> lastLend = getLastLendOfBookCopy(bookCopy);
        return !(lastLend.isEmpty() || lastLend.get().getLendStatus() == Lend.LendStatus.RETURNED);
    }

    public Optional<BookCopy> getAvailableCopyForLending(Book book) {
        return book.getCopies().stream().filter(bookCopy -> !isBookCurrentlyLend(bookCopy) && bookCopy.bookCondition != BookCopy.BookCondition.DETERIORATED).findFirst();
    }

    public void saveLend(Lend lend) {
        lendRepository.save(lend);
    }

    public void approveLendExtension(LendExtension lendExtension) {
        Lend correspondingLend = lendExtension.getLend();

        correspondingLend.setLendDuration(correspondingLend.getLendDuration() + lendExtension.getExtensionDuration());

        if (correspondingLend.getLendTime().plusDays(correspondingLend.getLendDuration() + 1).isAfter(LocalDateTime.now(ZoneOffset.UTC)) && correspondingLend.getLendStatus() == Lend.LendStatus.OVERDUE) {
            correspondingLend.setLendStatus(Lend.LendStatus.LENT);
        }

        saveLend(correspondingLend);
    }

    public void saveLendExtension(LendExtension lendExtension) {
        lendExtensionRepository.save(lendExtension);
    }

    public boolean canFriendBorrowBooks(Friend friend) {
        List<Lend> activeLendsOfFriend = getLendsByFriend(friend).stream().filter(lend -> lend.getLendStatus() != Lend.LendStatus.RETURNED).collect(Collectors.toList());
        return activeLendsOfFriend.size() < 10 || activeLendsOfFriend.stream().allMatch(lend -> lend.getLendStatus() != Lend.LendStatus.OVERDUE);
    }

    public boolean canExtensionBeRequestedForLend(Lend lend) {
        Optional<LendExtension> lastLendExtension = getLastExtensionForLend(lend);
        return lastLendExtension.isEmpty() || lastLendExtension.get().getExtensionStatus() != LendExtension.LendExtensionStatus.PENDING;
    }
}
