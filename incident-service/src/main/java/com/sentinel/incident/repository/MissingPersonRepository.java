package com.sentinel.incident.repository;

import com.sentinel.incident.entity.MissingPersonReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissingPersonRepository extends JpaRepository<MissingPersonReport, Long> {

    Optional<MissingPersonReport> findByCaseNumber(String caseNumber);

    List<MissingPersonReport> findAllByOrderByCreatedAtDesc();

    List<MissingPersonReport> findByStatus(String status);

    long countByStatus(String status);

    long countByCriticalRisk(Boolean criticalRisk);
}
