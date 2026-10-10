package com.sentinel.incident.service;

import com.sentinel.incident.entity.Personnel;
import com.sentinel.incident.entity.ResourceUnit;
import com.sentinel.incident.repository.PersonnelRepository;
import com.sentinel.incident.repository.ResourceUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final ResourceUnitRepository resourceUnitRepository;

    public PersonnelService(PersonnelRepository personnelRepository, ResourceUnitRepository resourceUnitRepository) {
        this.personnelRepository = personnelRepository;
        this.resourceUnitRepository = resourceUnitRepository;
    }

    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAllByOrderByFullNameAsc();
    }

    public List<ResourceUnit> getAllResources() {
        return resourceUnitRepository.findAll();
    }

    public long getAvailablePersonnelCount() {
        return personnelRepository.countByStatus("ON_DUTY");
    }

    public long getAvailableResourceCount() {
        return resourceUnitRepository.findByStatus("AVAILABLE").size();
    }
}
