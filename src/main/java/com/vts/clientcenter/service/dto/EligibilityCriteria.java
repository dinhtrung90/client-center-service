package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.vts.clientcenter.web.rest.admin.AdminEligibilityResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.Eligibility} entity. This class is used
 * in {@link AdminEligibilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eligibilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EligibilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter fullName;

    private StringFilter ssn;

    private InstantFilter birthDay;

    private StringFilter company;

    private StringFilter employeeId;

    private StringFilter otherCompany;

    public EligibilityCriteria() {
    }

    public EligibilityCriteria(EligibilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.ssn = other.ssn == null ? null : other.ssn.copy();
        this.birthDay = other.birthDay == null ? null : other.birthDay.copy();
    }

    @Override
    public EligibilityCriteria copy() {
        return new EligibilityCriteria(this);
    }


    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getSsn() {
        return ssn;
    }

    public void setSsn(StringFilter ssn) {
        this.ssn = ssn;
    }

    public InstantFilter getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(InstantFilter birthDay) {
        this.birthDay = birthDay;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public StringFilter getCompany() {
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public StringFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(StringFilter employeeId) {
        this.employeeId = employeeId;
    }

    public StringFilter getOtherCompany() {
        return otherCompany;
    }

    public void setOtherCompany(StringFilter otherCompany) {
        this.otherCompany = otherCompany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EligibilityCriteria that = (EligibilityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(ssn, that.ssn) &&
            Objects.equals(birthDay, that.birthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        email,
        phone,
        fullName,
        ssn,
        birthDay
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (ssn != null ? "ssn=" + ssn + ", " : "") +
                (birthDay != null ? "birthDay=" + birthDay + ", " : "") +
            "}";
    }

}
