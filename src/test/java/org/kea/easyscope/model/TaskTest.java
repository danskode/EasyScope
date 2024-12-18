package org.kea.easyscope.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // Unit test of method in Task to get at an end date on a task ...
    @Test
    public void testGetTaskEndDate_WithValidInputs() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1); // Start date
        int estimatedHours = 14; // Estimated hours (2 days)
        Task task = new Task();
        task.setTaskStartDate(startDate);
        task.setEstimatedHours(estimatedHours);

        // Act
        LocalDate endDate = task.getTaskEndDate();

        // Assert
        assertThat(endDate).isEqualTo(startDate.plusDays(2)); // 14 hours / 7 hours per day = 2 days
    }
}