package com.sentinel.incident.repository;

import com.sentinel.incident.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByOrderByCreatedAtDesc();

    List<Alert> findByAcknowledgedOrderByCreatedAtDesc(Boolean acknowledged);

    long countByAcknowledged(Boolean acknowledged);
}
