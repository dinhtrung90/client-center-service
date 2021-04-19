package com.vts.clientcenter.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "vts_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_initial")
    private String middleInitial;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "number_phone")
    private String numberPhone;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "zip")
    private String zip;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "department")
    private String department;

    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Employee employeeId(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Employee sourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public Employee middleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
        return this;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Employee emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public Employee numberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
        return this;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getStreet() {
        return street;
    }

    public Employee street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public Employee city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public Employee stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public Employee zip(String zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public Employee birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartment() {
        return department;
    }

    public Employee department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public Employee socialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
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
