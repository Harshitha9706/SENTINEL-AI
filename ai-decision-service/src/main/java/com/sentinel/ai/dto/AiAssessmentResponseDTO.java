package com.sentinel.ai.dto;

import java.util.Map;

public class AiAssessmentResponseDTO {

    private String incidentNumber;
    private String classification;
    private Double priorityScore;
    private String priorityLevel;
    private Boolean requiresImmediateHumanReview;
    private Map<String, Double> factorBreakdown;
    private String explanation;
    private Integer historicalOccurrences;
    private String suggestedResponse;
    private String modelType;

    public AiAssessmentResponseDTO() {}

    public String getIncidentNumber() { return incidentNumber; }
    public void setIncidentNumber(String incidentNumber) { this.incidentNumber = incidentNumber; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    public Boolean getRequiresImmediateHumanReview() { return requiresImmediateHumanReview; }
    public void setRequiresImmediateHumanReview(Boolean requiresImmediateHumanReview) { this.requiresImmediateHumanReview = requiresImmediateHumanReview; }

    public Map<String, Double> getFactorBreakdown() { return factorBreakdown; }
    public void setFactorBreakdown(Map<String, Double> factorBreakdown) { this.factorBreakdown = factorBreakdown; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public Integer getHistoricalOccurrences() { return historicalOccurrences; }
    public void setHistoricalOccurrences(Integer historicalOccurrences) { this.historicalOccurrences = historicalOccurrences; }

    public String getSuggestedResponse() { return suggestedResponse; }
    public void setSuggestedResponse(String suggestedResponse) { this.suggestedResponse = suggestedResponse; }

    public String getModelType() { return modelType; }
    public void setModelType(String modelType) { this.modelType = modelType; }
}
