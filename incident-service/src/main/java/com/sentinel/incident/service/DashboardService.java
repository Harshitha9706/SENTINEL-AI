package com.sentinel.incident.service;

import com.sentinel.incident.dto.DashboardSummaryDTO;
import com.sentinel.incident.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final IncidentRepository incidentRepository;
    private final CaseRepository caseRepository;
    private final AlertRepository alertRepository;
    private final HistoricalIncidentRepository historicalIncidentRepository;
    private final ResourceUnitRepository resourceUnitRepository;
    private final PersonnelRepository personnelRepository;
    private final MissingPersonRepository missingPersonRepository;

    public DashboardService(IncidentRepository incidentRepository, 
                            CaseRepository caseRepository, 
                            AlertRepository alertRepository, 
                            HistoricalIncidentRepository historicalIncidentRepository,
                            ResourceUnitRepository resourceUnitRepository,
                            PersonnelRepository personnelRepository,
                            MissingPersonRepository missingPersonRepository) {
        this.incidentRepository = incidentRepository;
        this.caseRepository = caseRepository;
        this.alertRepository = alertRepository;
        this.historicalIncidentRepository = historicalIncidentRepository;
        this.resourceUnitRepository = resourceUnitRepository;
        this.personnelRepository = personnelRepository;
        this.missingPersonRepository = missingPersonRepository;
    }

    public DashboardSummaryDTO getSummary() {
        long totalIncidents = incidentRepository.count();
        long openCases = caseRepository.countByStatus("OPEN");
        long highPriority = incidentRepository.countByPriorityLevel("HIGH");
        long unackAlerts = alertRepository.countByAcknowledged(false);
        long availableResources = resourceUnitRepository.findByStatus("AVAILABLE").size();
        long onDutyPersonnel = personnelRepository.countByStatus("ON_DUTY");
        long activeMissingPersons = missingPersonRepository.countByStatus("ACTIVE");

        Map<String, Long> statusDist = new HashMap<>();
        statusDist.put("REPORTED", incidentRepository.countByStatus("REPORTED"));
        statusDist.put("IN_PROGRESS", incidentRepository.countByStatus("IN_PROGRESS"));
        statusDist.put("RESOLVED", incidentRepository.countByStatus("RESOLVED"));
        statusDist.put("CLOSED", incidentRepository.countByStatus("CLOSED"));

        Map<String, Long> priorityDist = new HashMap<>();
        priorityDist.put("HIGH", incidentRepository.countByPriorityLevel("HIGH"));
        priorityDist.put("MEDIUM", incidentRepository.countByPriorityLevel("MEDIUM"));
        priorityDist.put("LOW", incidentRepository.countByPriorityLevel("LOW"));

        return new DashboardSummaryDTO(totalIncidents, openCases, highPriority, unackAlerts, 
                availableResources, onDutyPersonnel, activeMissingPersons, statusDist, priorityDist);
    }

    public List<Object[]> getHistoricalPatterns() {
        return historicalIncidentRepository.getCategoryFrequencyDistribution();
    }
}
