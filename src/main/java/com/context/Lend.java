package com.context;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lend")
public class Lend {
    @EmbeddedId
    private LendKey id;

    @ManyToOne
    @MapsId("bookId")
    @JsonManagedReference
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("friendId")
    @JsonManagedReference
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;

    @Column(name = "lend_time", nullable = false)
    private LocalDateTime lendTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "lend_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendStatus lendStatus;

    public Lend() {
    }

    public Lend(LendKey id, LocalDateTime lendTime, LocalDateTime returnTime, LendStatus lendStatus) {
        this.id = id;
        this.lendTime = lendTime;
        this.returnTime = returnTime;
        this.lendStatus = lendStatus;
    }

    public LocalDateTime getLendTime() {
        return lendTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public LendStatus getLendStatus() {
        return lendStatus;
    }

    public enum LendStatus {
        LENT,
        RETURNED
    }

    public enum SortingCriteria {
        LEND_TIME,
        RETURNED_TIME
    }
}
