package com.context;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "lend_extension")
public class LendExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "extension_reason", length = 500, nullable = false)
    private String reason;

    @Column(name = "extension_duration", nullable = false)
    @Min(0)
    private Integer extensionDuration;

    @Column(name = "extension_requested_time", nullable = false)
    private LocalDateTime requestedTime;

    @Column(name = "extension_response_time")
    private LocalDateTime responseTime;

    @Column(name = "extension_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LendExtensionStatus extensionStatus;

    @ManyToOne
    @JoinColumn(name = "lend_id", referencedColumnName = "id", nullable = false)
    private Lend lend;

    public LendExtension() {}

    public LendExtension(String reason, Integer extensionDuration, LocalDateTime requestedTime, LendExtensionStatus extensionStatus, Lend lend) {
        this.reason = reason;
        this.extensionDuration = extensionDuration;
        this.requestedTime = requestedTime;
        this.extensionStatus = extensionStatus;
        this.lend = lend;
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Integer getExtensionDuration() {
        return extensionDuration;
    }

    public LocalDateTime getRequestedTime() {
        return requestedTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public LendExtensionStatus getExtensionStatus() {
        return extensionStatus;
    }

    public void setExtensionStatus(LendExtensionStatus extensionStatus) {
        this.extensionStatus = extensionStatus;
    }

    public Lend getLend() {
        return lend;
    }

    public enum LendExtensionStatus {
        PENDING,
        DECLINED,
        APPROVED
    }
}
