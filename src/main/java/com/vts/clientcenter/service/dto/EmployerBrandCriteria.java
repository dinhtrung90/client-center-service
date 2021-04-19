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
 * Criteria class for the {@link com.vts.clientcenter.domain.EmployerBrand} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EmployerBrandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employer-brands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployerBrandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter logoUrl;

    private StringFilter primaryColor;

    private StringFilter displayName;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    private LongFilter employerId;

    public EmployerBrandCriteria() {
    }

    public EmployerBrandCriteria(EmployerBrandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
        this.primaryColor = other.primaryColor == null ? null : other.primaryColor.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.employerId = other.employerId == null ? null : other.employerId.copy();
    }

    @Override
    public EmployerBrandCriteria copy() {
        return new EmployerBrandCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public StringFilter getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(StringFilter primaryColor) {
        this.primaryColor = primaryColor;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployerBrandCriteria that = (EmployerBrandCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(primaryColor, that.primaryColor) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(employerId, that.employerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        logoUrl,
        primaryColor,
        displayName,
        createdDate,
        lastModifiedDate,
        createdBy,
        lastModifiedBy,
        employerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployerBrandCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
                (primaryColor != null ? "primaryColor=" + primaryColor + ", " : "") +
                (displayName != null ? "displayName=" + displayName + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (employerId != null ? "employerId=" + employerId + ", " : "") +
            "}";
    }

}
