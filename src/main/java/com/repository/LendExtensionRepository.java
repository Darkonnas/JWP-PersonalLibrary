package com.repository;

import com.context.Lend;
import com.context.LendExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LendExtensionRepository extends JpaRepository<LendExtension, Long> {
    List<LendExtension> findAllByLend(Lend lend);
}
