package com.abms.service;

import com.abms.repository.VaccinationRepository;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class VaccinationService {
    private final VaccinationRepository vaccinationRepository;

    public VaccinationService() {
        this.vaccinationRepository = new VaccinationRepository();
    }

    /**
     * Generates a standard vaccination schedule based on Day 0 (bite date/initial visit)
     * Days: 0, 3, 7, 14, 28
     */
    public void generateSchedule(int incidentId, LocalDate startDate) throws SQLException {
        int[] intervals = {0, 3, 7, 14, 28};
        
        for (int i = 0; i < intervals.length; i++) {
            LocalDate scheduledDate = startDate.plusDays(intervals[i]);
            vaccinationRepository.addVaccinationSchedule(incidentId, i + 1, Date.valueOf(scheduledDate));
        }
    }

    public void completeDose(int vaccinationId, int adminId) throws SQLException {
        vaccinationRepository.updateVaccinationStatus(vaccinationId, Date.valueOf(LocalDate.now()), "Completed", adminId);
    }
}
