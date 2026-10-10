package com.sentinel.incident.service;

import com.sentinel.incident.entity.Alert;
import com.sentinel.incident.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AuditLogService auditLogService;

    public AlertService(AlertRepository alertRepository, AuditLogService auditLogService) {
        this.alertRepository = alertRepository;
        this.auditLogService = auditLogService;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Alert> getUnacknowledgedAlerts() {
        return alertRepository.findByAcknowledgedOrderByCreatedAtDesc(false);
    }

    public Optional<Alert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    @Transactional
    public Alert acknowledgeAlert(Long id, String acknowledgedBy) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with ID: " + id));

        String officer = (acknowledgedBy != null && !acknowledgedBy.isBlank()) ? acknowledgedBy : "Duty Commander";
        alert.setAcknowledged(true);
        alert.setAcknowledgedBy(officer);
        alert.setAcknowledgedAt(LocalDateTime.now());
        Alert saved = alertRepository.save(alert);

        auditLogService.record("ALERT_ACKNOWLEDGED", "ALERT", String.valueOf(saved.getId()), 
                officer, "Acknowledged alert for Incident " + alert.getIncident().getIncidentNumber() + " (Level: " + alert.getAlertLevel() + ")");

        return saved;
    }

    public long getUnacknowledgedCount() {
        return alertRepository.countByAcknowledged(false);
    }
}
