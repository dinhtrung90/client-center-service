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
 * Criteria class for the {@link com.vts.clientcenter.domain.Employer} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EmployerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employerKey;

    private StringFilter name;

    private StringFilter address;

    private StringFilter longitude;

    private StringFilter latitude;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    private LongFilter employerDepartmentId;

    private LongFilter employeeId;

    private LongFilter employerBrandId;

    public EmployerCriteria() {
    }

    public EmployerCriteria(EmployerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employerKey = other.employerKey == null ? null : other.employerKey.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.employerDepartmentId = other.employerDepartmentId == null ? null : other.employerDepartmentId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.employerBrandId = other.employerBrandId == null ? null : other.employerBrandId.copy();
    }

    @Override
    public EmployerCriteria copy() {
        return new EmployerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmployerKey() {
        return employerKey;
    }

    public void setEmployerKey(StringFilter employerKey) {
        this.employerKey = employerKey;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
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

    public LongFilter getEmployerDepartmentId() {
        return employerDepartmentId;
    }

    public void setEmployerDepartmentId(LongFilter employerDepartmentId) {
        this.employerDepartmentId = employerDepartmentId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getEmployerBrandId() {
        return employerBrandId;
    }

    public void setEmployerBrandId(LongFilter employerBrandId) {
        this.employerBrandId = employerBrandId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployerCriteria that = (EmployerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(employerKey, that.employerKey) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(employerDepartmentId, that.employerDepartmentId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(employerBrandId, that.employerBrandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        employerKey,
        name,
        address,
        longitude,
        latitude,
        createdDate,
        lastModifiedDate,
        createdBy,
        lastModifiedBy,
        employerDepartmentId,
        employeeId,
        employerBrandId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (employerKey != null ? "employerKey=" + employerKey + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (employerDepartmentId != null ? "employerDepartmentId=" + employerDepartmentId + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
                (employerBrandId != null ? "employerBrandId=" + employerBrandId + ", " : "") +
            "}";
    }

}
