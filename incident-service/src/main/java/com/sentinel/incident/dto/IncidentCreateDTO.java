package com.sentinel.incident.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class IncidentCreateDTO {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must provide at least 10 characters of detail")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Severity is required")
    private String reportedSeverity; // LOW, MEDIUM, HIGH, CRITICAL

    private Boolean immediateThreat = false;

    public IncidentCreateDTO() {}

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getReportedSeverity() { return reportedSeverity; }
    public void setReportedSeverity(String reportedSeverity) { this.reportedSeverity = reportedSeverity; }

    public Boolean getImmediateThreat() { return immediateThreat; }
    public void setImmediateThreat(Boolean immediateThreat) { this.immediateThreat = immediateThreat; }
}
