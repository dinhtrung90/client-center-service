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

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.OrganizationBrand} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.OrganizationBrandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organization-brands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationBrandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter logoUrl;

    private StringFilter primaryColor;

    private StringFilter backgroundColor;

    private StringFilter organizationId;

    public OrganizationBrandCriteria() {
    }

    public OrganizationBrandCriteria(OrganizationBrandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
        this.primaryColor = other.primaryColor == null ? null : other.primaryColor.copy();
        this.backgroundColor = other.backgroundColor == null ? null : other.backgroundColor.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
    }

    @Override
    public OrganizationBrandCriteria copy() {
        return new OrganizationBrandCriteria(this);
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

    public StringFilter getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(StringFilter backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public StringFilter getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(StringFilter organizationId) {
        this.organizationId = organizationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationBrandCriteria that = (OrganizationBrandCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(primaryColor, that.primaryColor) &&
            Objects.equals(backgroundColor, that.backgroundColor) &&
            Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        logoUrl,
        primaryColor,
        backgroundColor,
        organizationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationBrandCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
                (primaryColor != null ? "primaryColor=" + primaryColor + ", " : "") +
                (backgroundColor != null ? "backgroundColor=" + backgroundColor + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            "}";
    }

}
