package com.sentinel.incident.service;

import com.sentinel.incident.dto.MissingPersonCreateDTO;
import com.sentinel.incident.entity.MissingPersonReport;
import com.sentinel.incident.repository.MissingPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MissingPersonService {

    private final MissingPersonRepository missingPersonRepository;
    private final AuditLogService auditLogService;
    private final Random random = new Random();

    public MissingPersonService(MissingPersonRepository missingPersonRepository, AuditLogService auditLogService) {
        this.missingPersonRepository = missingPersonRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public MissingPersonReport createReport(MissingPersonCreateDTO dto) {
        MissingPersonReport report = new MissingPersonReport();
        report.setCaseNumber(generateCaseNumber());
        report.setFullName(dto.getFullName());
        report.setAge(dto.getAge());
        report.setGender(dto.getGender());
        report.setLastSeenLocation(dto.getLastSeenLocation());
        report.setLastSeenDate(dto.getLastSeenDate() != null ? dto.getLastSeenDate() : LocalDateTime.now());
        report.setPhysicalDescription(dto.getPhysicalDescription());
        report.setCriticalRisk(dto.getCriticalRisk() != null && dto.getCriticalRisk());
        report.setStatus("ACTIVE");
        report.setReportingContact(dto.getReportingContact());
        report.setAssignedOfficer(dto.getAssignedOfficer() != null ? dto.getAssignedOfficer() : "Special Victims Unit");

        MissingPersonReport saved = missingPersonRepository.save(report);

        auditLogService.record("MISSING_PERSON_REPORTED", "MISSING_PERSON", saved.getCaseNumber(), 
                saved.getAssignedOfficer(), "New missing person report logged for " + saved.getFullName() + " (Age: " + saved.getAge() + ")");

        return saved;
    }

    public List<MissingPersonReport> getAllReports() {
        return missingPersonRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<MissingPersonReport> getById(Long id) {
        return missingPersonRepository.findById(id);
    }

    public long getActiveCount() {
        return missingPersonRepository.countByStatus("ACTIVE");
    }

    private String generateCaseNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randNum = 1000 + random.nextInt(9000);
        return "MP-" + datePrefix + "-" + randNum;
    }
}
