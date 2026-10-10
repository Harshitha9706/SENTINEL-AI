package com.sentinel.incident.dto;

import java.util.Map;

public class DashboardSummaryDTO {

    private long totalIncidents;
    private long openCases;
    private long highPriorityIncidents;
    private long unacknowledgedAlerts;
    private long availableResources;
    private long onDutyPersonnel;
    private long activeMissingPersons;
    private Map<String, Long> statusDistribution;
    private Map<String, Long> priorityDistribution;

    public DashboardSummaryDTO() {}

    public DashboardSummaryDTO(long totalIncidents, long openCases, long highPriorityIncidents, 
                               long unacknowledgedAlerts, long availableResources, long onDutyPersonnel,
                               long activeMissingPersons, Map<String, Long> statusDistribution, 
                               Map<String, Long> priorityDistribution) {
        this.totalIncidents = totalIncidents;
        this.openCases = openCases;
        this.highPriorityIncidents = highPriorityIncidents;
        this.unacknowledgedAlerts = unacknowledgedAlerts;
        this.availableResources = availableResources;
        this.onDutyPersonnel = onDutyPersonnel;
        this.activeMissingPersons = activeMissingPersons;
        this.statusDistribution = statusDistribution;
        this.priorityDistribution = priorityDistribution;
    }

    public long getTotalIncidents() { return totalIncidents; }
    public void setTotalIncidents(long totalIncidents) { this.totalIncidents = totalIncidents; }

    public long getOpenCases() { return openCases; }
    public void setOpenCases(long openCases) { this.openCases = openCases; }

    public long getHighPriorityIncidents() { return highPriorityIncidents; }
    public void setHighPriorityIncidents(long highPriorityIncidents) { this.highPriorityIncidents = highPriorityIncidents; }

    public long getUnacknowledgedAlerts() { return unacknowledgedAlerts; }
    public void setUnacknowledgedAlerts(long unacknowledgedAlerts) { this.unacknowledgedAlerts = unacknowledgedAlerts; }

    public long getAvailableResources() { return availableResources; }
    public void setAvailableResources(long availableResources) { this.availableResources = availableResources; }

    public long getOnDutyPersonnel() { return onDutyPersonnel; }
    public void setOnDutyPersonnel(long onDutyPersonnel) { this.onDutyPersonnel = onDutyPersonnel; }

    public long getActiveMissingPersons() { return activeMissingPersons; }
    public void setActiveMissingPersons(long activeMissingPersons) { this.activeMissingPersons = activeMissingPersons; }

    public Map<String, Long> getStatusDistribution() { return statusDistribution; }
    public void setStatusDistribution(Map<String, Long> statusDistribution) { this.statusDistribution = statusDistribution; }

    public Map<String, Long> getPriorityDistribution() { return priorityDistribution; }
    public void setPriorityDistribution(Map<String, Long> priorityDistribution) { this.priorityDistribution = priorityDistribution; }
}
