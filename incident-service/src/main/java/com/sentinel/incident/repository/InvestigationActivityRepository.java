package com.sentinel.incident.repository;

import com.sentinel.incident.entity.InvestigationActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestigationActivityRepository extends JpaRepository<InvestigationActivity, Long> {

    List<InvestigationActivity> findByCaseRecordIdOrderByLoggedAtDesc(Long caseId);
}
