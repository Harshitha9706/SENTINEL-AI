package com.sentinel.incident.repository;

import com.sentinel.incident.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    Optional<Personnel> findByBadgeNumber(String badgeNumber);

    List<Personnel> findAllByOrderByFullNameAsc();

    List<Personnel> findByDepartment(String department);

    List<Personnel> findByStatus(String status);

    long countByStatus(String status);
}
