package com.sentinel.incident.service;

import com.sentinel.incident.dto.AiAssessmentResponseDTO;
import com.sentinel.incident.dto.IncidentAnalysisRequestDTO;
import com.sentinel.incident.entity.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class AiIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(AiIntegrationService.class);

    private final RestClient restClient;
    private final String aiServiceUrl;

    public AiIntegrationService(RestClient restClient, 
                                @Value("${sentinel.ai.service.url:http://localhost:8081}") String aiServiceUrl) {
        this.restClient = restClient;
        this.aiServiceUrl = aiServiceUrl;
    }

    /**
     * Calls the AI Decision Service over REST.
     * Guaranteed resilience: if AI service is offline or throws error, returns Optional.empty()
     * so incident creation is never blocked or aborted.
     */
    public Optional<AiAssessmentResponseDTO> requestAssessment(Incident incident) {
        try {
            IncidentAnalysisRequestDTO request = new IncidentAnalysisRequestDTO(
                    incident.getIncidentNumber(),
                    incident.getCategory(),
                    incident.getDescription(),
                    incident.getLocation(),
                    incident.getReportedAt(),
                    incident.getReportedSeverity(),
                    incident.getImmediateThreat()
            );

            log.info("Dispatching REST analysis request to AI Service for incident: {}", incident.getIncidentNumber());

            AiAssessmentResponseDTO response = restClient.post()
                    .uri(aiServiceUrl + "/api/ai/analyze")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiAssessmentResponseDTO.class);

            return Optional.ofNullable(response);
        } catch (Exception ex) {
            log.warn("AI Decision Service is currently unavailable or returned error ({}). Entering resilient fallback mode.", ex.getMessage());
            return Optional.empty();
        }
    }
}
