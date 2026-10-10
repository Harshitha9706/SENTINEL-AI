package com.sentinel.incident.repository;

import com.sentinel.incident.entity.ResourceUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceUnitRepository extends JpaRepository<ResourceUnit, Long> {

    Optional<ResourceUnit> findByUnitIdentifier(String unitIdentifier);

    List<ResourceUnit> findByStatus(String status);
}
