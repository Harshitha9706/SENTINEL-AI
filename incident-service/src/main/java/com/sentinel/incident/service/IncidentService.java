package com.sentinel.incident.service;

import com.sentinel.incident.dto.AiAssessmentResponseDTO;
import com.sentinel.incident.dto.IncidentCreateDTO;
import com.sentinel.incident.entity.Alert;
import com.sentinel.incident.entity.Incident;
import com.sentinel.incident.repository.AlertRepository;
import com.sentinel.incident.repository.IncidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);

    private final IncidentRepository incidentRepository;
    private final AlertRepository alertRepository;
    private final AiIntegrationService aiIntegrationService;
    private final AuditLogService auditLogService;
    private final Random random = new Random();

    public IncidentService(IncidentRepository incidentRepository, 
                           AlertRepository alertRepository, 
                           AiIntegrationService aiIntegrationService,
                           AuditLogService auditLogService) {
        this.incidentRepository = incidentRepository;
        this.alertRepository = alertRepository;
        this.aiIntegrationService = aiIntegrationService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Incident registerIncident(IncidentCreateDTO dto) {
        Incident incident = new Incident();
        incident.setIncidentNumber(generateIncidentNumber());
        incident.setCategory(dto.getCategory());
        incident.setDescription(dto.getDescription());
        incident.setLocation(dto.getLocation());
        incident.setReportedSeverity(dto.getReportedSeverity());
        incident.setImmediateThreat(dto.getImmediateThreat() != null && dto.getImmediateThreat());
        incident.setStatus("REPORTED");
        incident.setReportedAt(LocalDateTime.now());
        incident.setAiAssessmentStatus("PENDING");

        // Step 1: Persist incident in database first to guarantee zero data loss
        Incident saved = incidentRepository.save(incident);
        log.info("Incident {} persisted with ID {}", saved.getIncidentNumber(), saved.getId());

        auditLogService.record("INCIDENT_INGESTION", "INCIDENT", saved.getIncidentNumber(), 
                "System Intake", "Registered incident: " + saved.getCategory() + " at " + saved.getLocation() + " (Severity: " + saved.getReportedSeverity() + ")");

        // Step 2: Attempt AI Assessment over REST
        evaluateAiAssessment(saved);

        // Step 3: Trigger Alert if immediate threat or high priority
        checkAndTriggerAlert(saved);

        return incidentRepository.save(saved);
    }

    @Transactional
    public Incident retryAiAssessment(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + incidentId));

        evaluateAiAssessment(incident);
        checkAndTriggerAlert(incident);
        
        auditLogService.record("AI_RETRY_EVALUATION", "INCIDENT", incident.getIncidentNumber(), 
                "Duty Officer", "Re-triggered AI priority evaluation. New status: " + incident.getAiAssessmentStatus());

        return incidentRepository.save(incident);
    }

    private void evaluateAiAssessment(Incident incident) {
        Optional<AiAssessmentResponseDTO> assessmentOpt = aiIntegrationService.requestAssessment(incident);

        if (assessmentOpt.isPresent()) {
            AiAssessmentResponseDTO assessment = assessmentOpt.get();
            incident.setAiAssessmentStatus("COMPLETED");
            incident.setPriorityScore(assessment.getPriorityScore());
            incident.setPriorityLevel(assessment.getPriorityLevel());
            incident.setAiExplanation(assessment.getExplanation());
            incident.setAiRecommendation(assessment.getSuggestedResponse());
            log.info("Incident {} successfully analyzed by AI: Score={}, Level={}", 
                    incident.getIncidentNumber(), assessment.getPriorityScore(), assessment.getPriorityLevel());
            
            auditLogService.record("AI_ASSESSMENT_COMPLETED", "INCIDENT", incident.getIncidentNumber(), 
                    "AI Decision Engine (:8081)", "Assessed Priority: " + assessment.getPriorityLevel() + " (" + assessment.getPriorityScore() + " pts)");
        } else {
            incident.setAiAssessmentStatus("UNAVAILABLE");
            incident.setAiExplanation("AI Decision Service currently unavailable. Incident queued for manual triage or deferred AI assessment.");
            log.warn("AI assessment marked UNAVAILABLE for incident {}", incident.getIncidentNumber());

            auditLogService.record("AI_ASSESSMENT_UNAVAILABLE", "INCIDENT", incident.getIncidentNumber(), 
                    "System Fallback", "AI Decision Service unreachable; incident preserved in MySQL with status UNAVAILABLE.");
        }
    }

    private void checkAndTriggerAlert(Incident incident) {
        if (Boolean.TRUE.equals(incident.getImmediateThreat())) {
            Alert alert = new Alert(incident, "CRITICAL", 
                    "IMMEDIATE LIFE SAFETY THREAT reported at " + incident.getLocation() + " (" + incident.getCategory() + ")");
            alertRepository.save(alert);
            log.info("Critical alert automatically generated for incident {}", incident.getIncidentNumber());
            
            auditLogService.record("ALERT_TRIGGERED", "ALERT", incident.getIncidentNumber(), 
                    "Safety Override Engine", "Critical immediate threat alert dispatched.");
        } else if ("HIGH".equalsIgnoreCase(incident.getPriorityLevel())) {
            Alert alert = new Alert(incident, "HIGH", 
                    "High priority AI assessment (" + incident.getPriorityScore() + " pts) for incident " + incident.getIncidentNumber());
            alertRepository.save(alert);

            auditLogService.record("ALERT_TRIGGERED", "ALERT", incident.getIncidentNumber(), 
                    "Priority Engine", "High priority alert dispatched.");
        }
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Incident> searchIncidents(String status, String priority, String keyword) {
        String cleanStatus = (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) ? status : null;
        String cleanPriority = (priority != null && !priority.isBlank() && !priority.equalsIgnoreCase("ALL")) ? priority : null;
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        return incidentRepository.searchIncidents(cleanStatus, cleanPriority, cleanKeyword);
    }

    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }

    @Transactional
    public Incident updateStatus(Long id, String newStatus) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found with ID: " + id));
        String oldStatus = incident.getStatus();
        incident.setStatus(newStatus.toUpperCase());
        Incident saved = incidentRepository.save(incident);

        auditLogService.record("STATUS_UPDATED", "INCIDENT", incident.getIncidentNumber(), 
                "Duty Officer", "Status transitioned from " + oldStatus + " to " + newStatus.toUpperCase());

        return saved;
    }

    private String generateIncidentNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randNum = 1000 + random.nextInt(9000);
        return "INC-" + datePrefix + "-" + randNum;
    }
}
