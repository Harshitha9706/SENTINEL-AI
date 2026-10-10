package com.sentinel.incident.service;

import com.sentinel.incident.dto.ActivityCreateDTO;
import com.sentinel.incident.dto.CaseCreateDTO;
import com.sentinel.incident.entity.CaseRecord;
import com.sentinel.incident.entity.Incident;
import com.sentinel.incident.entity.InvestigationActivity;
import com.sentinel.incident.repository.CaseRepository;
import com.sentinel.incident.repository.IncidentRepository;
import com.sentinel.incident.repository.InvestigationActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CaseService {

    private final CaseRepository caseRepository;
    private final IncidentRepository incidentRepository;
    private final InvestigationActivityRepository activityRepository;
    private final AuditLogService auditLogService;
    private final Random random = new Random();

    public CaseService(CaseRepository caseRepository, 
                       IncidentRepository incidentRepository, 
                       InvestigationActivityRepository activityRepository,
                       AuditLogService auditLogService) {
        this.caseRepository = caseRepository;
        this.incidentRepository = incidentRepository;
        this.activityRepository = activityRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public CaseRecord createCase(CaseCreateDTO dto) {
        Incident incident = incidentRepository.findById(dto.getIncidentId())
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + dto.getIncidentId()));

        CaseRecord caseRecord = new CaseRecord();
        caseRecord.setCaseNumber(generateCaseNumber());
        caseRecord.setIncident(incident);
        caseRecord.setTitle(dto.getTitle());
        caseRecord.setLeadInvestigator(dto.getLeadInvestigator());
        caseRecord.setDeadline(dto.getDeadline());
        caseRecord.setStatus("OPEN");

        CaseRecord saved = caseRepository.save(caseRecord);

        // Update incident status to IN_PROGRESS
        incident.setStatus("IN_PROGRESS");
        incidentRepository.save(incident);

        // Add initial system activity
        InvestigationActivity initialActivity = new InvestigationActivity();
        initialActivity.setCaseRecord(saved);
        initialActivity.setActivityType("CASE_OPENED");
        initialActivity.setActivityNotes("Formal investigation case opened from Incident " + incident.getIncidentNumber());
        initialActivity.setLoggedBy(dto.getLeadInvestigator());
        activityRepository.save(initialActivity);

        auditLogService.record("CASE_OPENED", "CASE", saved.getCaseNumber(), 
                dto.getLeadInvestigator(), "Case created for Incident " + incident.getIncidentNumber() + ": " + saved.getTitle());

        return saved;
    }

    public List<CaseRecord> getAllCases() {
        return caseRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<CaseRecord> getCaseById(Long id) {
        return caseRepository.findById(id);
    }

    public List<InvestigationActivity> getActivitiesForCase(Long caseId) {
        return activityRepository.findByCaseRecordIdOrderByLoggedAtDesc(caseId);
    }

    @Transactional
    public InvestigationActivity logActivity(Long caseId, ActivityCreateDTO dto) {
        CaseRecord caseRecord = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found: " + caseId));

        InvestigationActivity activity = new InvestigationActivity();
        activity.setCaseRecord(caseRecord);
        activity.setActivityType(dto.getActivityType());
        activity.setActivityNotes(dto.getActivityNotes());
        activity.setLoggedBy(dto.getLoggedBy());
        InvestigationActivity saved = activityRepository.save(activity);

        auditLogService.record("INVESTIGATION_ACTIVITY_LOGGED", "CASE", caseRecord.getCaseNumber(), 
                dto.getLoggedBy(), "Logged activity (" + dto.getActivityType() + "): " + dto.getActivityNotes());

        return saved;
    }

    private String generateCaseNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randNum = 1000 + random.nextInt(9000);
        return "CASE-" + datePrefix + "-" + randNum;
    }
}
