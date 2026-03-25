package com.abms.service;

public class VaccinationRecord {
    private int id;
    private String patientName;
    private int doseNumber;
    private String scheduledDate;
    private String status;

    public VaccinationRecord(int id, String patientName, int doseNumber, String scheduledDate, String status) {
        this.id = id;
        this.patientName = patientName;
        this.doseNumber = doseNumber;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    public int getId() { return id; }
    public String getPatientName() { return patientName; }
    public int getDoseNumber() { return doseNumber; }
    public String getScheduledDate() { return scheduledDate; }
    public String getStatus() { return status; }
}
