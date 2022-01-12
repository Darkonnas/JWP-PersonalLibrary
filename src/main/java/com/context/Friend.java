package com.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = false)
    private String lastName;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
    @JsonIgnore
    private List<Lend> lends;

    public Friend() {
    }

    public Long getId() {
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
