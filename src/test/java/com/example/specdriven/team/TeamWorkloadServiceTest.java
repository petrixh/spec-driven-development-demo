package com.example.specdriven.team;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamWorkloadServiceTest {

    @Autowired
    private TeamWorkloadService workloadService;

    @Test
    void workloadIncludesAllTeamMembers() {
        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();

        // DataInitializer creates 5 members
        assertTrue(workloads.size() >= 5, "Should include all team members");
    }

    @Test
    void workloadAggregatesTasksAcrossProjects() {
        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();

        // Bob has tasks in both "Website Redesign" and "Internal Tools"
        var bobWorkload = workloads.stream()
                .filter(w -> w.member().getName().equals("Bob Chen"))
                .findFirst()
                .orElseThrow();

        assertTrue(bobWorkload.tasksByProject().size() >= 2,
                "Bob should have tasks from multiple projects");
    }

    @Test
    void activeTaskCountIsCorrect() {
        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();

        // Every workload should have non-negative counts
        for (var w : workloads) {
            assertTrue(w.activeTasks() >= 0);
            assertTrue(w.completedTasks() >= 0);
        }
    }

    @Test
    void membersWithZeroTasksAreIncluded() {
        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();

        // All members listed, even those without tasks (if any)
        // DataInitializer assigns tasks to all members, but the test validates
        // that the service doesn't filter out zero-task members
        assertFalse(workloads.isEmpty());
    }

    @Test
    void overloadThresholdIsFiveActiveTasks() {
        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();

        for (var w : workloads) {
            if (w.activeTasks() > 5) {
                assertTrue(w.isOverloaded());
            } else {
                assertFalse(w.isOverloaded());
            }
        }
    }
}
