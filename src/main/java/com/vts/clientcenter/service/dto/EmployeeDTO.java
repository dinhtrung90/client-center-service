package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {
    private Long id;

    private String employeeId;

    private String sourceId;

    private String firstName;

    private String lastName;

    private String middleInitial;

    private String emailAddress;

    private String numberPhone;

    private String street;

    private String city;

    private String stateCode;

    private String zip;

    private Instant birthDate;

    private String department;

    private String socialSecurityNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", employeeId='" + getEmployeeId() + "'" +
            ", sourceId='" + getSourceId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleInitial='" + getMiddleInitial() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", numberPhone='" + getNumberPhone() + "'" +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", zip='" + getZip() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", department='" + getDepartment() + "'" +
            ", socialSecurityNumber='" + getSocialSecurityNumber() + "'" +
            "}";
    }
}
