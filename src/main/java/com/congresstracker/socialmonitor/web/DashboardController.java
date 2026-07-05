package com.congresstracker.socialmonitor.web;

import com.congresstracker.socialmonitor.dto.DashboardDtos.DashboardStats;
import com.congresstracker.socialmonitor.service.DashboardService;
import com.congresstracker.socialmonitor.service.MockSyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final MockSyncService mockSyncService;

    public DashboardController(DashboardService dashboardService, MockSyncService mockSyncService) {
        this.dashboardService = dashboardService;
        this.mockSyncService = mockSyncService;
    }

    @GetMapping("/stats")
    public DashboardStats stats() {
        return dashboardService.stats();
    }

    @PostMapping("/sync")
    public Map<String, Object> syncNow() {
        int syncedAccounts = mockSyncService.simulateSync();
        return Map.of("syncedAccounts", syncedAccounts, "message", "Mock social sync completed");
    }
}
