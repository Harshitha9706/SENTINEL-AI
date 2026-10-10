package com.sentinel.incident.controller;

import com.sentinel.incident.dto.*;
import com.sentinel.incident.entity.*;
import com.sentinel.incident.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class IncidentRestController {

    private final IncidentService incidentService;
    private final CaseService caseService;
    private final AlertService alertService;
    private final DashboardService dashboardService;
    private final MissingPersonService missingPersonService;
    private final AuditLogService auditLogService;

    public IncidentRestController(IncidentService incidentService, 
                                  CaseService caseService, 
                                  AlertService alertService, 
                                  DashboardService dashboardService,
                                  MissingPersonService missingPersonService,
                                  AuditLogService auditLogService) {
        this.incidentService = incidentService;
        this.caseService = caseService;
        this.alertService = alertService;
        this.dashboardService = dashboardService;
        this.missingPersonService = missingPersonService;
        this.auditLogService = auditLogService;
    }

    // --- 1. INCIDENTS API ---

    @PostMapping("/incidents")
    public ResponseEntity<Incident> registerIncident(@Valid @RequestBody IncidentCreateDTO dto) {
        Incident created = incidentService.registerIncident(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/incidents")
    public ResponseEntity<List<Incident>> getIncidents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String query) {
        List<Incident> list = incidentService.searchIncidents(status, priority, query);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/incidents/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/incidents/{id}/status")
    public ResponseEntity<Incident> updateIncidentStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateDTO dto) {
        Incident updated = incidentService.updateStatus(id, dto.getStatus());
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/incidents/{id}/retry-ai")
    public ResponseEntity<Incident> retryAiAssessment(@PathVariable Long id) {
        Incident retried = incidentService.retryAiAssessment(id);
        return ResponseEntity.ok(retried);
    }

    // --- 2. CASES API ---

    @PostMapping("/cases")
    public ResponseEntity<CaseRecord> createCase(@Valid @RequestBody CaseCreateDTO dto) {
        CaseRecord caseRecord = caseService.createCase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(caseRecord);
    }

    @GetMapping("/cases")
    public ResponseEntity<List<CaseRecord>> getCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @GetMapping("/cases/{id}")
    public ResponseEntity<CaseRecord> getCaseById(@PathVariable Long id) {
        return caseService.getCaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cases/{id}/activities")
    public ResponseEntity<InvestigationActivity> logCaseActivity(
            @PathVariable Long id, 
            @Valid @RequestBody ActivityCreateDTO dto) {
        InvestigationActivity activity = caseService.logActivity(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    }

    // --- 3. DASHBOARD & ANALYTICS API ---

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/analytics/patterns")
    public ResponseEntity<List<Object[]>> getPatterns() {
        return ResponseEntity.ok(dashboardService.getHistoricalPatterns());
    }

    // --- 4. MISSING PERSONS API ---

    @PostMapping("/missing-persons")
    public ResponseEntity<MissingPersonReport> createMissingPersonReport(@Valid @RequestBody MissingPersonCreateDTO dto) {
        MissingPersonReport report = missingPersonService.createReport(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/missing-persons")
    public ResponseEntity<List<MissingPersonReport>> getMissingPersons() {
        return ResponseEntity.ok(missingPersonService.getAllReports());
    }

    // --- 5. ALERTS API ---

    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getAlerts(@RequestParam(required = false, defaultValue = "false") Boolean unacknowledgedOnly) {
        if (unacknowledgedOnly) {
            return ResponseEntity.ok(alertService.getUnacknowledgedAlerts());
        }
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PatchMapping("/alerts/{id}/acknowledge")
    public ResponseEntity<Alert> acknowledgeAlert(@PathVariable Long id, @RequestBody(required = false) AlertAcknowledgeDTO dto) {
        String officer = (dto != null && dto.getAcknowledgedBy() != null) ? dto.getAcknowledgedBy() : "Duty Officer";
        Alert updated = alertService.acknowledgeAlert(id, officer);
        return ResponseEntity.ok(updated);
    }

    // --- 6. AUDIT LOGS API ---

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }
}
