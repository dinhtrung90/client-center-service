package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.Employer} entity.
 */
public class EmployerDTO implements Serializable {
    private Long id;

    @Size(max = 100)
    private String employerKey;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String address;

    private String street;

    private String city;

    private String county;

    @Size(max = 100)
    private String longitude;

    @Size(max = 100)
    private String latitude;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployerKey() {
        return employerKey;
    }

    public void setEmployerKey(String employerKey) {
        this.employerKey = employerKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployerDTO employerDTO = (EmployerDTO) o;
        if (employerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "EmployerDTO{" +
            "id=" +
            getId() +
            ", employerKey='" +
            getEmployerKey() +
            "'" +
            ", name='" +
            getName() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", phone='" +
            getPhone() +
            "'" +
            ", address='" +
            getAddress() +
            "'" +
            ", street='" +
            getStreet() +
            "'" +
            ", city='" +
            getCity() +
            "'" +
            ", county='" +
            getCounty() +
            "'" +
            ", longitude='" +
            getLongitude() +
            "'" +
            ", latitude='" +
            getLatitude() +
            "'" +
            ", createdDate='" +
            getCreatedDate() +
            "'" +
            ", lastModifiedDate='" +
            getLastModifiedDate() +
            "'" +
            ", createdBy='" +
            getCreatedBy() +
            "'" +
            ", lastModifiedBy='" +
            getLastModifiedBy() +
            "'" +
            "}"
        );
    }
}
