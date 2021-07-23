package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.enumeration.Gender;

import java.time.Instant;
import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.Eligibility} entity.
 */
public class EligibilityDTO implements Serializable {

    private String id;

    @Size(max = 50)
    private String email;

    @NotNull
    @Size(max = 30)
    private String phone;

    @Size(max = 50)
    private String fullName;

    @NotNull
    @Size(max = 20)
    private String ssn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT_YYYY_MM_DD)
    private Date birthDay;

    private Gender gender;

    private String fullAddress;

    private String employeeId;

    private String company;

    private String otherCompany;

    private String code;

    private Instant expiredDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOtherCompany() {
        return otherCompany;
    }

    public void setOtherCompany(String otherCompany) {
        this.otherCompany = otherCompany;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Instant expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibilityDTO)) {
            return false;
        }

        return id != null && id.equals(((EligibilityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", ssn='" + getSsn() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            "}";
    }
}
