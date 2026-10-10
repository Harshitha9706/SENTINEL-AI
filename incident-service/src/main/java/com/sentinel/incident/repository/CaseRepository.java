package com.sentinel.incident.repository;

import com.sentinel.incident.entity.CaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<CaseRecord, Long> {

    Optional<CaseRecord> findByCaseNumber(String caseNumber);

    List<CaseRecord> findAllByOrderByCreatedAtDesc();

    List<CaseRecord> findByStatus(String status);

    long countByStatus(String status);
}
