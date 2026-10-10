package com.sentinel.incident.dto;

import jakarta.validation.constraints.NotBlank;

public class AlertAcknowledgeDTO {

    @NotBlank(message = "Acknowledged by officer name is required")
    private String acknowledgedBy;

    public AlertAcknowledgeDTO() {}

    public AlertAcknowledgeDTO(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }
}
