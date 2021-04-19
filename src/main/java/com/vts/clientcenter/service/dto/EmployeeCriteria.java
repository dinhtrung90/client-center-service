package com.vts.clientcenter.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.Employee} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeId;

    private StringFilter sourceId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter middleInitial;

    private StringFilter emailAddress;

    private StringFilter numberPhone;

    private StringFilter street;

    private StringFilter city;

    private StringFilter stateCode;

    private StringFilter zip;

    private InstantFilter birthDate;

    private StringFilter department;

    private StringFilter socialSecurityNumber;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.sourceId = other.sourceId == null ? null : other.sourceId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.middleInitial = other.middleInitial == null ? null : other.middleInitial.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.numberPhone = other.numberPhone == null ? null : other.numberPhone.copy();
        this.street = other.street == null ? null : other.street.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateCode = other.stateCode == null ? null : other.stateCode.copy();
        this.zip = other.zip == null ? null : other.zip.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.department = other.department == null ? null : other.department.copy();
        this.socialSecurityNumber = other.socialSecurityNumber == null ? null : other.socialSecurityNumber.copy();
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(StringFilter employeeId) {
        this.employeeId = employeeId;
    }

    public StringFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(StringFilter sourceId) {
        this.sourceId = sourceId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(StringFilter middleInitial) {
        this.middleInitial = middleInitial;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(StringFilter numberPhone) {
        this.numberPhone = numberPhone;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateCode() {
        return stateCode;
    }

    public void setStateCode(StringFilter stateCode) {
        this.stateCode = stateCode;
    }

    public StringFilter getZip() {
        return zip;
    }

    public void setZip(StringFilter zip) {
        this.zip = zip;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(StringFilter socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(middleInitial, that.middleInitial) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(numberPhone, that.numberPhone) &&
            Objects.equals(street, that.street) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateCode, that.stateCode) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(department, that.department) &&
            Objects.equals(socialSecurityNumber, that.socialSecurityNumber)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeId,
            sourceId,
            firstName,
            lastName,
            middleInitial,
            emailAddress,
            numberPhone,
            street,
            city,
            stateCode,
            zip,
            birthDate,
            department,
            socialSecurityNumber
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (middleInitial != null ? "middleInitial=" + middleInitial + ", " : "") +
                (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
                (numberPhone != null ? "numberPhone=" + numberPhone + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateCode != null ? "stateCode=" + stateCode + ", " : "") +
                (zip != null ? "zip=" + zip + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (department != null ? "department=" + department + ", " : "") +
                (socialSecurityNumber != null ? "socialSecurityNumber=" + socialSecurityNumber + ", " : "") +
            "}";
    }
}
