package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

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

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    private LongFilter employerId;

    private LongFilter employerDepartmentId;

    public EmployeeCriteria() {
    }

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
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
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.employerId = other.employerId == null ? null : other.employerId.copy();
        this.employerDepartmentId = other.employerDepartmentId == null ? null : other.employerDepartmentId.copy();
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

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LongFilter getEmployerId() {
        return employerId;
    }

    public void setEmployerId(LongFilter employerId) {
        this.employerId = employerId;
    }

    public LongFilter getEmployerDepartmentId() {
        return employerDepartmentId;
    }

    public void setEmployerDepartmentId(LongFilter employerDepartmentId) {
        this.employerDepartmentId = employerDepartmentId;
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
        return
            Objects.equals(id, that.id) &&
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
            Objects.equals(socialSecurityNumber, that.socialSecurityNumber) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(employerId, that.employerId) &&
            Objects.equals(employerDepartmentId, that.employerDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
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
        socialSecurityNumber,
        createdDate,
        lastModifiedDate,
        createdBy,
        lastModifiedBy,
        employerId,
        employerDepartmentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
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
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (employerId != null ? "employerId=" + employerId + ", " : "") +
                (employerDepartmentId != null ? "employerDepartmentId=" + employerDepartmentId + ", " : "") +
            "}";
    }

}
