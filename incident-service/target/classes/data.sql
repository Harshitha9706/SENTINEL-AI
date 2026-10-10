-- Database initialization and baseline seed data for SENTINEL AI

-- 1. Synthetic Historical Incidents for Pattern Analytics
INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Armed Robbery', 'Sector 4 Commercial Plaza', 90, 4, '2026-08-15 14:30:00', 'ARRESTED_ON_SCENE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 1);

INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Armed Robbery', 'Sector 4 Commercial Plaza', 85, 6, '2026-08-28 20:15:00', 'RECOVERED_PROPERTY', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 2);

INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Assault', 'Sector 4 Commercial Plaza', 70, 3, '2026-09-05 23:10:00', 'SUSPECT_IDENTIFIED', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 3);

INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Armed Robbery', 'Sector 4 Commercial Plaza', 95, 2, '2026-09-19 18:45:00', 'ARRESTED_ON_SCENE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 4);

INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Vehicle Theft', 'Sector 2 Downtown', 50, 12, '2026-09-22 03:00:00', 'VEHICLE_RECOVERED', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 5);

INSERT INTO historical_incidents (category, location_zone, severity_weight, resolution_time_hours, occurred_at, outcome, is_synthetic_demonstration)
SELECT 'Burglary', 'Sector 7 Residential', 60, 8, '2026-09-25 11:20:00', 'UNDER_INVESTIGATION', TRUE
WHERE NOT EXISTS (SELECT 1 FROM historical_incidents WHERE id = 6);

-- 2. Baseline Public Safety Resources
INSERT INTO resources (unit_identifier, unit_type, status, assigned_location, last_ping)
SELECT 'UNIT-ALPHA-01', 'PATROL_VEHICLE', 'AVAILABLE', 'Sector 2 Downtown', NOW()
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE id = 1);

INSERT INTO resources (unit_identifier, unit_type, status, assigned_location, last_ping)
SELECT 'UNIT-BRAVO-04', 'TACTICAL_UNIT', 'AVAILABLE', 'Central Command HQ', NOW()
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE id = 2);

INSERT INTO resources (unit_identifier, unit_type, status, assigned_location, last_ping)
SELECT 'UNIT-MEDIC-02', 'EMS_AMBULANCE', 'AVAILABLE', 'Metro General Post', NOW()
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE id = 3);

INSERT INTO resources (unit_identifier, unit_type, status, assigned_location, last_ping)
SELECT 'UNIT-INVEST-09', 'INVESTIGATION_UNIT', 'AVAILABLE', 'CID Headquarters', NOW()
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE id = 4);
