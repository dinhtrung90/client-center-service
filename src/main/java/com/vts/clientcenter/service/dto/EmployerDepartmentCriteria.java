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
 * Criteria class for the {@link com.vts.clientcenter.domain.EmployerDepartment} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EmployerDepartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employer-departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployerDepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter departmentName;

    private InstantFilter createdDate;

    private InstantFilter lstModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    private LongFilter employerId;

    public EmployerDepartmentCriteria() {
    }

    public EmployerDepartmentCriteria(EmployerDepartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lstModifiedDate = other.lstModifiedDate == null ? null : other.lstModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.employerId = other.employerId == null ? null : other.employerId.copy();
    }

    @Override
    public EmployerDepartmentCriteria copy() {
        return new EmployerDepartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLstModifiedDate() {
        return lstModifiedDate;
    }

    public void setLstModifiedDate(InstantFilter lstModifiedDate) {
        this.lstModifiedDate = lstModifiedDate;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployerDepartmentCriteria that = (EmployerDepartmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lstModifiedDate, that.lstModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(employerId, that.employerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        departmentName,
        createdDate,
        lstModifiedDate,
        createdBy,
        lastModifiedBy,
        employerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployerDepartmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lstModifiedDate != null ? "lstModifiedDate=" + lstModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (employerId != null ? "employerId=" + employerId + ", " : "") +
            "}";
    }

}
