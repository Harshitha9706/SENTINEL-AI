package com.sentinel.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "investigation_activities")
public class InvestigationActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private CaseRecord caseRecord;

    @Column(name = "activity_type", nullable = false, length = 64)
    private String activityType;

    @Column(name = "activity_notes", nullable = false, columnDefinition = "TEXT")
    private String activityNotes;

    @Column(name = "logged_by", nullable = false, length = 128)
    private String loggedBy;

    @Column(name = "logged_at", nullable = false, updatable = false)
    private LocalDateTime loggedAt;

    public InvestigationActivity() {}

    @PrePersist
    protected void onCreate() {
        this.loggedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CaseRecord getCaseRecord() { return caseRecord; }
    public void setCaseRecord(CaseRecord caseRecord) { this.caseRecord = caseRecord; }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public String getActivityNotes() { return activityNotes; }
    public void setActivityNotes(String activityNotes) { this.activityNotes = activityNotes; }

    public String getLoggedBy() { return loggedBy; }
    public void setLoggedBy(String loggedBy) { this.loggedBy = loggedBy; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}
