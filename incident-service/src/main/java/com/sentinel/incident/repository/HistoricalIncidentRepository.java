package com.sentinel.incident.repository;

import com.sentinel.incident.entity.HistoricalIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricalIncidentRepository extends JpaRepository<HistoricalIncident, Long> {

    List<HistoricalIncident> findByCategory(String category);

    List<HistoricalIncident> findByLocationZone(String locationZone);

    long countByCategoryAndLocationZone(String category, String locationZone);

    @Query("SELECT COUNT(h) FROM HistoricalIncident h WHERE " +
           "LOWER(h.category) = LOWER(:category) AND " +
           "LOWER(h.locationZone) LIKE LOWER(CONCAT('%', :locationZone, '%')) AND " +
           "h.occurredAt >= :since")
    long countRecurrence(@Param("category") String category, 
                         @Param("locationZone") String locationZone, 
                         @Param("since") LocalDateTime since);

    @Query("SELECT h.category, COUNT(h) FROM HistoricalIncident h GROUP BY h.category ORDER BY COUNT(h) DESC")
    List<Object[]> getCategoryFrequencyDistribution();
}
