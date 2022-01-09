package com.repository;

import com.context.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findAllByLevel(long level);

    List<Shelf> findAllByRack(long rack);

    List<Shelf> findAllByRoom(long room);

    List<Shelf> findAllByStartingLetter(char startingLetter);
}
