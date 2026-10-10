package com.sentinel.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class ResourceUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_identifier", unique = true, nullable = false, length = 64)
    private String unitIdentifier;

    @Column(name = "unit_type", nullable = false, length = 64)
    private String unitType; // PATROL_VEHICLE, TACTICAL_UNIT, EMS_AMBULANCE, INVESTIGATION_UNIT

    @Column(nullable = false, length = 32)
    private String status = "AVAILABLE"; // AVAILABLE, DISPATCHED, ON_SCENE, MAINTENANCE

    @Column(name = "assigned_location", length = 255)
    private String assignedLocation;

    @Column(name = "last_ping")
    private LocalDateTime lastPing;

    public ResourceUnit() {}

    public ResourceUnit(String unitIdentifier, String unitType, String status, String assignedLocation) {
        this.unitIdentifier = unitIdentifier;
        this.unitType = unitType;
        this.status = status;
        this.assignedLocation = assignedLocation;
        this.lastPing = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastPing = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUnitIdentifier() { return unitIdentifier; }
    public void setUnitIdentifier(String unitIdentifier) { this.unitIdentifier = unitIdentifier; }

    public String getUnitType() { return unitType; }
    public void setUnitType(String unitType) { this.unitType = unitType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedLocation() { return assignedLocation; }
    public void setAssignedLocation(String assignedLocation) { this.assignedLocation = assignedLocation; }

    public LocalDateTime getLastPing() { return lastPing; }
    public void setLastPing(LocalDateTime lastPing) { this.lastPing = lastPing; }
}
