package com.abms.model;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String address;
    private String contactNumber;

    public Patient(int id, String firstName, String lastName, int age, String gender, String address, String contactNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
}
