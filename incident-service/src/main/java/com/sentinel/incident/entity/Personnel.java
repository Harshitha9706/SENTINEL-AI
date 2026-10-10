package com.sentinel.incident.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "personnel")
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "badge_number", unique = true, nullable = false, length = 32)
    private String badgeNumber;

    @NotBlank
    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(name = "rank_title", nullable = false, length = 64)
    private String rankTitle; // DETECTIVE, SERGEANT, PATROL_OFFICER, COMMANDER, DISPATCHER

    @Column(nullable = false, length = 64)
    private String department; // PATROL_DIVISION, CID, TACTICAL_UNIT, DISPATCH_OPS

    @Column(nullable = false, length = 32)
    private String status = "ON_DUTY"; // ON_DUTY, DISPATCHED, OFF_DUTY, ON_LEAVE

    @Column(name = "contact_radio", length = 64)
    private String contactRadio;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Personnel() {}

    public Personnel(String badgeNumber, String fullName, String rankTitle, String department, String status, String contactRadio) {
        this.badgeNumber = badgeNumber;
        this.fullName = fullName;
        this.rankTitle = rankTitle;
        this.department = department;
        this.status = status;
        this.contactRadio = contactRadio;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBadgeNumber() { return badgeNumber; }
    public void setBadgeNumber(String badgeNumber) { this.badgeNumber = badgeNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRankTitle() { return rankTitle; }
    public void setRankTitle(String rankTitle) { this.rankTitle = rankTitle; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getContactRadio() { return contactRadio; }
    public void setContactRadio(String contactRadio) { this.contactRadio = contactRadio; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
