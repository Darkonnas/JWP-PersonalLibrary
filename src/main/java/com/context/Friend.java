package com.context;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = false)
    private String lastName;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
    private List<Lend> lends;

    public Friend() {
    }

    public Friend(long id, String firstName, String lastName, String address, List<Lend> lentBooks) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.lends = lentBooks;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public List<Lend> getLends() {
        return lends;
    }

    public void updateFriend(Friend friend) {
        this.firstName = friend.firstName;
        this.lastName = friend.lastName;
        this.address = friend.address;
        this.lends = friend.lends;
    }

    public enum SortingCriteria {
        FIRST_NAME,
        LAST_NAME
    }
}
