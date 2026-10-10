package com.sentinel.incident.service;

import com.sentinel.incident.entity.AuditLog;
import com.sentinel.incident.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLog record(String actionType, String entityName, String entityIdentifier, String performedBy, String details) {
        AuditLog log = new AuditLog(actionType, entityName, entityIdentifier, performedBy, details);
        return auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}
