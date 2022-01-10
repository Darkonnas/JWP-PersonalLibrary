package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "lend")
public class Lend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lend")
    @JsonBackReference
    private Set<LendExtension> lendExtensions;

    @Column(name = "lend_time", nullable = false)
    private LocalDateTime lendTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "lend_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendStatus lendStatus;

    public Lend() {
    }

    public Lend(long id, Set<LendExtension> lendExtensions, LocalDateTime lendTime, LocalDateTime returnTime, LendStatus lendStatus) {
        this.id = id;
        this.lendExtensions = lendExtensions;
        this.lendTime = lendTime;
        this.returnTime = returnTime;
        this.lendStatus = lendStatus;
    }

    public Set<LendExtension> getLendExtensions() {
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
