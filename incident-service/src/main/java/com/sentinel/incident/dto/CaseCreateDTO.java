package com.sentinel.incident.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CaseCreateDTO {

    @NotNull(message = "Incident ID is required")
    private Long incidentId;

    @NotBlank(message = "Case title is required")
    private String title;

    @NotBlank(message = "Lead investigator is required")
    private String leadInvestigator;

    private LocalDate deadline;

    public CaseCreateDTO() {}

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLeadInvestigator() { return leadInvestigator; }
    public void setLeadInvestigator(String leadInvestigator) { this.leadInvestigator = leadInvestigator; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
