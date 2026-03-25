package com.abms.service;

import com.abms.repository.BiteIncidentRepository;
import com.abms.repository.PatientRepository;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientService {
    private final PatientRepository patientRepository;
    private final BiteIncidentRepository incidentRepository;
    private final VaccinationService vaccinationService;

    public PatientService() {
        this.patientRepository = new PatientRepository();
        this.incidentRepository = new BiteIncidentRepository();
        this.vaccinationService = new VaccinationService();
    }

    public void registerPatientWithIncident(
            String firstName, String lastName, int age, String gender, String address, String contact,
            LocalDate biteDate, String animalType, String biteSite, String animalStatus, String exposureType, String remarks
    ) throws SQLException {
        
        // 1. Add Patient
        int patientId = patientRepository.addPatient(firstName, lastName, age, gender, address, contact);
        
        // 2. Add Incident
        int incidentId = incidentRepository.addIncident(patientId, Date.valueOf(biteDate), animalType, biteSite, animalStatus, exposureType, remarks);
        
        // 3. Generate Vaccination Schedule
        vaccinationService.generateSchedule(incidentId, biteDate);
    }
}
