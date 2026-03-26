package com.abms.model;

import java.time.LocalDate;

public class BiteIncident {
    private int id;
    private int patientId;
    private String patientName;
    private LocalDate biteDate;
    private String animalType;
    private String biteSite;
    private String animalStatus;
    private String exposureType;
    private String remarks;

    public BiteIncident(int id, int patientId, String patientName, LocalDate biteDate, String animalType, String biteSite, String animalStatus, String exposureType, String remarks) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.biteDate = biteDate;
        this.animalType = animalType;
        this.biteSite = biteSite;
        this.animalStatus = animalStatus;
        this.exposureType = exposureType;
        this.remarks = remarks;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public LocalDate getBiteDate() { return biteDate; }
    public String getAnimalType() { return animalType; }
    public String getBiteSite() { return biteSite; }
    public String getAnimalStatus() { return animalStatus; }
    public String getExposureType() { return exposureType; }
    public String getRemarks() { return remarks; }
}
