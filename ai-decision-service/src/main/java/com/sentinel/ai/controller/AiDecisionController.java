package com.sentinel.ai.controller;

import com.sentinel.ai.dto.AiAssessmentResponseDTO;
import com.sentinel.ai.dto.IncidentAnalysisRequestDTO;
import com.sentinel.ai.service.ExplainableScoringEngine;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiDecisionController {

    private final ExplainableScoringEngine scoringEngine;

    public AiDecisionController(ExplainableScoringEngine scoringEngine) {
        this.scoringEngine = scoringEngine;
    }

    /**
     * Core AI Assessment Endpoint
     * Synchronously invoked by Incident Management Service on Port 8080.
     */
    @PostMapping("/analyze")
    public ResponseEntity<AiAssessmentResponseDTO> analyzeIncident(@Valid @RequestBody IncidentAnalysisRequestDTO request) {
        AiAssessmentResponseDTO assessment = scoringEngine.evaluate(request);
        return ResponseEntity.ok(assessment);
    }

    /**
     * Pattern Analysis Endpoint
     */
    @GetMapping("/patterns")
    public ResponseEntity<Map<String, Object>> getHistoricalPatterns(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String zone) {
        Map<String, Object> patterns = new HashMap<>();
        patterns.put("category", category != null ? category : "All Categories");
        patterns.put("zone", zone != null ? zone : "All Zones");
        patterns.put("hotspotIdentified", true);
        patterns.put("primaryHotspot", "Sector 4 Commercial Plaza");
        patterns.put("frequencyScore", 85.0);
        patterns.put("syntheticDemoNotice", "Pattern analytics derived from simulated baseline demonstration dataset.");
        return ResponseEntity.ok(patterns);
    }

    /**
     * Response Recommendations Endpoint
     */
    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, String>> getRecommendations(@RequestBody Map<String, String> request) {
        String category = request.getOrDefault("category", "General");
        String severity = request.getOrDefault("severity", "MEDIUM");

        Map<String, String> response = new HashMap<>();
        response.put("category", category);
        response.put("tacticalAction", "Dispatch nearest patrol unit and maintain audio monitoring with caller.");
        response.put("suggestedUnits", "PATROL_VEHICLE, CID_INVESTIGATOR");
        return ResponseEntity.ok(response);
    }
}
