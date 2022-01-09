package com.repository;

import com.context.Lend;
import com.context.LendKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LendRepository extends JpaRepository<Lend, LendKey> {
    List<Lend> findAllByLendTimeBefore(LocalDateTime lendTime);

    List<Lend> findAllByLendTimeAfter(LocalDateTime lendTime);

    List<Lend> findAllByReturnTimeBefore(LocalDateTime returnTime);

    List<Lend> findAllByReturnTimeAfter(LocalDateTime returnTime);

    List<Lend> findAllByLendStatus(Lend.LendStatus lendStatus);
}
