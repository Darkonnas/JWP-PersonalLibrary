package com.repository;

import com.context.Friend;
import com.context.Lend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LendRepository extends JpaRepository<Lend, Long> {
    List<Lend> findAllByFriend(Friend friend);
}
