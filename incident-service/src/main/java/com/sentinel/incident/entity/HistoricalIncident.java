package com.sentinel.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historical_incidents")
public class HistoricalIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(name = "location_zone", nullable = false, length = 128)
    private String locationZone;

    @Column(name = "severity_weight", nullable = false)
    private Integer severityWeight;

    @Column(name = "resolution_time_hours", nullable = false)
    private Integer resolutionTimeHours;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(nullable = false, length = 64)
    private String outcome;

    @Column(name = "is_synthetic_demonstration", nullable = false)
    private Boolean isSyntheticDemonstration = true;

    public HistoricalIncident() {}

    public HistoricalIncident(String category, String locationZone, Integer severityWeight, 
                              Integer resolutionTimeHours, LocalDateTime occurredAt, String outcome) {
        this.category = category;
        this.locationZone = locationZone;
        this.severityWeight = severityWeight;
        this.resolutionTimeHours = resolutionTimeHours;
        this.occurredAt = occurredAt;
        this.outcome = outcome;
        this.isSyntheticDemonstration = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocationZone() { return locationZone; }
    public void setLocationZone(String locationZone) { this.locationZone = locationZone; }

    public Integer getSeverityWeight() { return severityWeight; }
    public void setSeverityWeight(Integer severityWeight) { this.severityWeight = severityWeight; }

    public Integer getResolutionTimeHours() { return resolutionTimeHours; }
    public void setResolutionTimeHours(Integer resolutionTimeHours) { this.resolutionTimeHours = resolutionTimeHours; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public Boolean getIsSyntheticDemonstration() { return isSyntheticDemonstration; }
    public void setIsSyntheticDemonstration(Boolean isSyntheticDemonstration) { this.isSyntheticDemonstration = isSyntheticDemonstration; }
}
