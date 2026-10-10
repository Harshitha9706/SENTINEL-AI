package com.sentinel.incident.repository;

import com.sentinel.incident.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    Optional<Incident> findByIncidentNumber(String incidentNumber);

    List<Incident> findAllByOrderByCreatedAtDesc();

    List<Incident> findByStatus(String status);

    List<Incident> findByPriorityLevel(String priorityLevel);

    long countByStatus(String status);

    long countByPriorityLevel(String priorityLevel);

    @Query("SELECT i FROM Incident i WHERE " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:priority IS NULL OR i.priorityLevel = :priority) AND " +
           "(:keyword IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.incidentNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY i.createdAt DESC")
    List<Incident> searchIncidents(@Param("status") String status, 
                                  @Param("priority") String priority, 
                                  @Param("keyword") String keyword);
}
