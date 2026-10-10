package com.sentinel.incident.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MissingPersonCreateDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Last seen location is required")
    private String lastSeenLocation;

    private LocalDateTime lastSeenDate;
    private String physicalDescription;
    private Boolean criticalRisk = false;
    private String reportingContact;
    private String assignedOfficer;

    public MissingPersonCreateDTO() {}

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

    public String getReportingContact() { return reportingContact; }
    public void setReportingContact(String reportingContact) { this.reportingContact = reportingContact; }

    public String getAssignedOfficer() { return assignedOfficer; }
    public void setAssignedOfficer(String assignedOfficer) { this.assignedOfficer = assignedOfficer; }
}
