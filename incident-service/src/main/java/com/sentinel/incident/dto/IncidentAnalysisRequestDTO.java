package com.sentinel.incident.dto;

import java.time.LocalDateTime;

public class IncidentAnalysisRequestDTO {

    private String incidentNumber;
    private String category;
    private String description;
    private String location;
    private LocalDateTime reportedAt;
    private String reportedSeverity;
    private Boolean immediateThreat;

    public IncidentAnalysisRequestDTO() {}

    public IncidentAnalysisRequestDTO(String incidentNumber, String category, String description,
                                      String location, LocalDateTime reportedAt, String reportedSeverity,
                                      Boolean immediateThreat) {
        this.incidentNumber = incidentNumber;
        this.category = category;
        this.description = description;
        this.location = location;
        this.reportedAt = reportedAt;
        this.reportedSeverity = reportedSeverity;
        this.immediateThreat = immediateThreat;
    }

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
}
