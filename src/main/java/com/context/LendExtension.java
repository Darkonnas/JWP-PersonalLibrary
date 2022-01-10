package com.context;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "lend_extension")
public class LendExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "extension_reason", length = 500, nullable = false)
    private String reason;

    @Column(name = "extension_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendExtensionStatus extensionStatus;

    @ManyToOne
    @JsonBackReference("lend")
    @JoinColumn(name = "lend_id", referencedColumnName = "id", nullable = false)
    private Lend lend;

    public LendExtension() {}

    public enum LendExtensionStatus {
        PENDING,
        DECLINED,
        APPROVED
    }
}
