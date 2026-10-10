package com.sentinel.incident.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "missing_person_reports")
public class MissingPersonReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_number", unique = true, nullable = false, length = 32)
    private String caseNumber;

    @NotBlank
    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 32)
    private String gender;

    @Column(name = "last_seen_location", nullable = false, length = 255)
    private String lastSeenLocation;

    @Column(name = "last_seen_date", nullable = false)
    private LocalDateTime lastSeenDate;

    @Column(name = "physical_description", columnDefinition = "TEXT")
    private String physicalDescription;

    @Column(name = "critical_risk", nullable = false)
    private Boolean criticalRisk = false; // Minor, elderly, medical condition

    @Column(nullable = false, length = 32)
    private String status = "ACTIVE"; // ACTIVE, LOCATED, CLOSED

    @Column(name = "reporting_contact", length = 128)
    private String reportingContact;

    @Column(name = "assigned_officer", length = 128)
    private String assignedOfficer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MissingPersonReport() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.lastSeenDate == null) {
            this.lastSeenDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCaseNumber() { return caseNumber; }
    public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLastSeenLocation() { return lastSeenLocation; }
    public void setLastSeenLocation(String lastSeenLocation) { this.lastSeenLocation = lastSeenLocation; }

    public LocalDateTime getLastSeenDate() { return lastSeenDate; }
    public void setLastSeenDate(LocalDateTime lastSeenDate) { this.lastSeenDate = lastSeenDate; }

    public String getPhysicalDescription() { return physicalDescription; }
    public void setPhysicalDescription(String physicalDescription) { this.physicalDescription = physicalDescription; }

    public Boolean getCriticalRisk() { return criticalRisk; }
    public void setCriticalRisk(Boolean criticalRisk) { this.criticalRisk = criticalRisk; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReportingContact() { return reportingContact; }
    public void setReportingContact(String reportingContact) { this.reportingContact = reportingContact; }

    public String getAssignedOfficer() { return assignedOfficer; }
    public void setAssignedOfficer(String assignedOfficer) { this.assignedOfficer = assignedOfficer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
