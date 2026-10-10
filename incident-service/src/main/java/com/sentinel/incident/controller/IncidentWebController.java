package com.sentinel.incident.controller;

import com.sentinel.incident.dto.ActivityCreateDTO;
import com.sentinel.incident.dto.CaseCreateDTO;
import com.sentinel.incident.dto.IncidentCreateDTO;
import com.sentinel.incident.dto.MissingPersonCreateDTO;
import com.sentinel.incident.entity.*;
import com.sentinel.incident.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IncidentWebController {

    private final IncidentService incidentService;
    private final CaseService caseService;
    private final AlertService alertService;
    private final DashboardService dashboardService;
    private final PersonnelService personnelService;
    private final MissingPersonService missingPersonService;
    private final AuditLogService auditLogService;

    public IncidentWebController(IncidentService incidentService, 
                                 CaseService caseService, 
                                 AlertService alertService, 
                                 DashboardService dashboardService,
                                 PersonnelService personnelService,
                                 MissingPersonService missingPersonService,
                                 AuditLogService auditLogService) {
        this.incidentService = incidentService;
        this.caseService = caseService;
        this.alertService = alertService;
        this.dashboardService = dashboardService;
        this.personnelService = personnelService;
        this.missingPersonService = missingPersonService;
        this.auditLogService = auditLogService;
    }

    // 1. DASHBOARD
    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("summary", dashboardService.getSummary());
        model.addAttribute("recentIncidents", incidentService.getAllIncidents().stream().limit(5).toList());
        model.addAttribute("unackAlerts", alertService.getUnacknowledgedAlerts());
        model.addAttribute("historicalPatterns", dashboardService.getHistoricalPatterns());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "dashboard";
    }

    // 2. INCIDENT LISTING & SEARCH
    @GetMapping("/incidents")
    public String listIncidents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String query,
            Model model) {
        List<Incident> incidents = incidentService.searchIncidents(status, priority, query);
        model.addAttribute("incidents", incidents);
        model.addAttribute("selectedStatus", status != null ? status : "ALL");
        model.addAttribute("selectedPriority", priority != null ? priority : "ALL");
        model.addAttribute("searchQuery", query != null ? query : "");
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "incidents/list";
    }

    // 3. INCIDENT REGISTRATION FORM
    @GetMapping("/incidents/new")
    public String showCreateIncidentForm(Model model) {
        if (!model.containsAttribute("incidentForm")) {
            model.addAttribute("incidentForm", new IncidentCreateDTO());
        }
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "incidents/create";
    }

    @PostMapping("/incidents")
    public String handleCreateIncident(
            @Valid @ModelAttribute("incidentForm") IncidentCreateDTO form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
            return "incidents/create";
        }

        try {
            Incident created = incidentService.registerIncident(form);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Incident registered successfully: " + created.getIncidentNumber() + 
                    " (AI Assessment: " + created.getAiAssessmentStatus() + ")");
            return "redirect:/incidents/" + created.getId();
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error saving incident: " + ex.getMessage());
            model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
            return "incidents/create";
        }
    }

    // 4. INCIDENT DETAIL VIEW
    @GetMapping("/incidents/{id}")
    public String showIncidentDetail(@PathVariable Long id, Model model) {
        Incident incident = incidentService.getIncidentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + id));
        model.addAttribute("incident", incident);
        model.addAttribute("caseForm", new CaseCreateDTO());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "incidents/detail";
    }

    @PostMapping("/incidents/{id}/status")
    public String updateIncidentStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        incidentService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Incident status updated to " + status);
        return "redirect:/incidents/" + id;
    }

    @PostMapping("/incidents/{id}/retry-ai")
    public String retryIncidentAi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Incident updated = incidentService.retryAiAssessment(id);
        redirectAttributes.addFlashAttribute("successMessage", "AI assessment re-evaluated: Status=" + updated.getAiAssessmentStatus());
        return "redirect:/incidents/" + id;
    }

    // 5. CASE MANAGEMENT
    @GetMapping("/cases")
    public String listCases(Model model) {
        List<CaseRecord> cases = caseService.getAllCases();
        model.addAttribute("cases", cases);
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "cases/list";
    }

    @PostMapping("/cases")
    public String createCase(@Valid @ModelAttribute("caseForm") CaseCreateDTO form, 
                             BindingResult bindingResult, 
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Case creation failed: Check all mandatory fields.");
            return "redirect:/incidents/" + form.getIncidentId();
        }
        CaseRecord caseRecord = caseService.createCase(form);
        redirectAttributes.addFlashAttribute("successMessage", "Case " + caseRecord.getCaseNumber() + " created and assigned to " + form.getLeadInvestigator());
        return "redirect:/cases";
    }

    @PostMapping("/cases/{id}/activities")
    public String logCaseActivityWeb(@PathVariable Long id, 
                                     @ModelAttribute ActivityCreateDTO form, 
                                     RedirectAttributes redirectAttributes) {
        caseService.logActivity(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Investigation activity recorded.");
        return "redirect:/cases";
    }

    // 6. PATTERN ANALYTICS
    @GetMapping("/analytics")
    public String showAnalytics(Model model) {
        model.addAttribute("patterns", dashboardService.getHistoricalPatterns());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "analytics/patterns";
    }

    // 7. PERSONNEL & RESOURCES
    @GetMapping("/resources")
    public String listResources(Model model) {
        model.addAttribute("personnel", personnelService.getAllPersonnel());
        model.addAttribute("resources", personnelService.getAllResources());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "resources/list";
    }

    // 8. MISSING PERSONS
    @GetMapping("/missing-persons")
    public String listMissingPersons(Model model) {
        model.addAttribute("missingPersons", missingPersonService.getAllReports());
        if (!model.containsAttribute("missingPersonForm")) {
            model.addAttribute("missingPersonForm", new MissingPersonCreateDTO());
        }
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "missing_persons/list";
    }

    @PostMapping("/missing-persons")
    public String createMissingPerson(@Valid @ModelAttribute("missingPersonForm") MissingPersonCreateDTO form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("missingPersons", missingPersonService.getAllReports());
            model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
            return "missing_persons/list";
        }
        MissingPersonReport report = missingPersonService.createReport(form);
        redirectAttributes.addFlashAttribute("successMessage", "Missing person report " + report.getCaseNumber() + " registered for " + report.getFullName());
        return "redirect:/missing-persons";
    }

    // 9. ALERTS
    @GetMapping("/alerts")
    public String showAlerts(Model model) {
        model.addAttribute("alerts", alertService.getAllAlerts());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "alerts/list";
    }

    @PostMapping("/alerts/{id}/acknowledge")
    public String acknowledgeAlert(@PathVariable Long id, 
                                   @RequestParam(defaultValue = "Duty Commander") String acknowledgedBy, 
                                   RedirectAttributes redirectAttributes) {
        alertService.acknowledgeAlert(id, acknowledgedBy);
        redirectAttributes.addFlashAttribute("successMessage", "Alert acknowledged successfully.");
        return "redirect:/alerts";
    }

    // 10. AUDIT LOGS
    @GetMapping("/audit-logs")
    public String showAuditLogs(Model model) {
        model.addAttribute("logs", auditLogService.getAllLogs());
        model.addAttribute("unackCount", alertService.getUnacknowledgedCount());
        return "audit_logs/list";
    }
}
