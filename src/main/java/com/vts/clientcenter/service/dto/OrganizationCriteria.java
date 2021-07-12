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
 * Criteria class for the {@link com.vts.clientcenter.domain.Organization} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.OrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter name;

    private StringFilter displayName;

    private StringFilter description;

    private StringFilter email;

    private StringFilter phone;

    private LongFilter userAddressId;

    private LongFilter organizationBrandId;

    private LongFilter organizationGroupId;

    public OrganizationCriteria() {
    }

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.userAddressId = other.userAddressId == null ? null : other.userAddressId.copy();
        this.organizationBrandId = other.organizationBrandId == null ? null : other.organizationBrandId.copy();
        this.organizationGroupId = other.organizationGroupId == null ? null : other.organizationGroupId.copy();
    }

    @Override
    public OrganizationCriteria copy() {
        return new OrganizationCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(LongFilter userAddressId) {
        this.userAddressId = userAddressId;
    }

    public LongFilter getOrganizationBrandId() {
        return organizationBrandId;
    }

    public void setOrganizationBrandId(LongFilter organizationBrandId) {
        this.organizationBrandId = organizationBrandId;
    }

    public LongFilter getOrganizationGroupId() {
        return organizationGroupId;
    }

    public void setOrganizationGroupId(LongFilter organizationGroupId) {
        this.organizationGroupId = organizationGroupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationCriteria that = (OrganizationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(userAddressId, that.userAddressId) &&
            Objects.equals(organizationBrandId, that.organizationBrandId) &&
            Objects.equals(organizationGroupId, that.organizationGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        displayName,
        description,
        email,
        phone,
        userAddressId,
        organizationBrandId,
        organizationGroupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (displayName != null ? "displayName=" + displayName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (userAddressId != null ? "userAddressId=" + userAddressId + ", " : "") +
                (organizationBrandId != null ? "organizationBrandId=" + organizationBrandId + ", " : "") +
                (organizationGroupId != null ? "organizationGroupId=" + organizationGroupId + ", " : "") +
            "}";
    }

}
