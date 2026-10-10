package com.sentinel.incident.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_number", unique = true, nullable = false, length = 32)
    private String incidentNumber;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 64)
    private String category;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 255)
    private String location;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "reported_severity", nullable = false, length = 32)
    private String reportedSeverity;

    @Column(name = "immediate_threat", nullable = false)
    private Boolean immediateThreat = false;

    @Column(nullable = false, length = 32)
    private String status = "REPORTED"; // REPORTED, IN_PROGRESS, RESOLVED, CLOSED

    @Column(name = "ai_assessment_status", nullable = false, length = 32)
    private String aiAssessmentStatus = "PENDING"; // PENDING, COMPLETED, UNAVAILABLE, FAILED

    @Column(name = "priority_score")
    private Double priorityScore;

    @Column(name = "priority_level", length = 32)
    private String priorityLevel; // HIGH, MEDIUM, LOW

    @Column(name = "ai_explanation", columnDefinition = "TEXT")
    private String aiExplanation;

    @Column(name = "ai_recommendation", columnDefinition = "TEXT")
    private String aiRecommendation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Incident() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.reportedAt == null) {
            this.reportedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIncidentNumber() { return incidentNumber; }
    public void setIncidentNumber(String incidentNumber) { this.incidentNumber = incidentNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public String getReportedSeverity() { return reportedSeverity; }
    public void setReportedSeverity(String reportedSeverity) { this.reportedSeverity = reportedSeverity; }

    public Boolean getImmediateThreat() { return immediateThreat; }
    public void setImmediateThreat(Boolean immediateThreat) { this.immediateThreat = immediateThreat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAiAssessmentStatus() { return aiAssessmentStatus; }
    public void setAiAssessmentStatus(String aiAssessmentStatus) { this.aiAssessmentStatus = aiAssessmentStatus; }

    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    public String getAiExplanation() { return aiExplanation; }
    public void setAiExplanation(String aiExplanation) { this.aiExplanation = aiExplanation; }

    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
