package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lend")
public class Lend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lend")
    @JsonIgnore
    private List<LendExtension> lendExtensions;

    @Column(name = "lend_time", nullable = false)
    private LocalDateTime lendTime;

    @Column(name = "lend_duration", nullable = false)
    @Min(0)
    private Integer lendDuration;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "lend_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendStatus lendStatus;

    public Lend() {
    }

    public Lend(BookCopy bookCopy, Friend friend, LocalDateTime lendTime, Integer lendDuration, LendStatus lendStatus) {
        this.bookCopy = bookCopy;
        this.friend = friend;
        this.lendTime = lendTime;
        this.lendDuration = lendDuration;
        this.lendStatus = lendStatus;
    }

    public Long getId() {
        return id;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public Friend getFriend() {
        return friend;
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

    public Integer getLendDuration() {
        return lendDuration;
    }

    public LendStatus getLendStatus() {
        return lendStatus;
    }

    public void setLendStatus(LendStatus lendStatus) {
        this.lendStatus = lendStatus;
    }

    public void setLendExtensions(List<LendExtension> lendExtensions) {
        this.lendExtensions = lendExtensions;
    }

    public enum LendStatus {
        LENT,
        OVERDUE,
        RETURNED
    }

    public void setLendDuration(Integer lendDuration) {
        this.lendDuration = lendDuration;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public enum SortingCriteria {
        LEND_TIME,
        RETURNED_TIME
    }
}
