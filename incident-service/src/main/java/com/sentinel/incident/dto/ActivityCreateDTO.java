package com.sentinel.incident.dto;

import jakarta.validation.constraints.NotBlank;

public class ActivityCreateDTO {

    @NotBlank(message = "Activity type is required")
    private String activityType;

    @NotBlank(message = "Activity notes are required")
    private String activityNotes;

    @NotBlank(message = "Logged by officer name is required")
    private String loggedBy;

    public ActivityCreateDTO() {}

    public ActivityCreateDTO(String activityType, String activityNotes, String loggedBy) {
        this.activityType = activityType;
        this.activityNotes = activityNotes;
        this.loggedBy = loggedBy;
    }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public String getActivityNotes() { return activityNotes; }
    public void setActivityNotes(String activityNotes) { this.activityNotes = activityNotes; }

    public String getLoggedBy() { return loggedBy; }
    public void setLoggedBy(String loggedBy) { this.loggedBy = loggedBy; }
}
