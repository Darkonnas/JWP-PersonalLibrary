package com.context;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LendKey implements Serializable {
    @Column(name = "book_id")
    private long bookId;

    @Column(name = "friend_id")
    private long friendId;

    public LendKey() {
    }

    public LendKey(long bookId, long friendId) {
        this.bookId = bookId;
        this.friendId = friendId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getFriendId() {
        return friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LendKey lendKey = (LendKey) o;
        return bookId == lendKey.bookId && friendId == lendKey.friendId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, friendId);
    }
}
