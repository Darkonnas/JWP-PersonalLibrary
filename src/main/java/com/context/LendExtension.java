package com.context;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "lend_extension")
public class LendExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "extension_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendExtensionStatus extensionStatus;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "lend_id", referencedColumnName = "id", nullable = false)
    private Lend lend;

    public enum LendExtensionStatus {
        PENDING,
        DECLINED,
        APPROVED
    }
}
