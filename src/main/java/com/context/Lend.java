package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lend")
public class Lend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonBackReference("book_copy")
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @ManyToOne
    @JsonBackReference("friend")
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lend")
    private List<LendExtension> lendExtensions;

    @Column(name = "lend_time", nullable = false)
    private LocalDateTime lendTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "lend_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendStatus lendStatus;

    public Lend() {
    }

    public List<LendExtension> getLendExtensions() {
        return lendExtensions;
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
        OVERDUE,
        RETURNED
    }

    public enum SortingCriteria {
        LEND_TIME,
        RETURNED_TIME
    }
}
