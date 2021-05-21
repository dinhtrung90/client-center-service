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
 * Criteria class for the {@link com.vts.clientcenter.domain.RolePermission} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.RolePermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /role-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RolePermissionCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter roleName;

    private LongFilter permissionId;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    public RolePermissionCriteria() {}

    public RolePermissionCriteria(RolePermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.roleName = other.roleName == null ? null : other.roleName.copy();
        this.permissionId = other.permissionId == null ? null : other.permissionId.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
    }

    @Override
    public RolePermissionCriteria copy() {
        return new RolePermissionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRoleName() {
        return roleName;
    }

    public void setRoleName(StringFilter roleName) {
        this.roleName = roleName;
    }

    public LongFilter getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(LongFilter permissionId) {
        this.permissionId = permissionId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RolePermissionCriteria that = (RolePermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roleName, that.roleName) &&
            Objects.equals(permissionId, that.permissionId) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, permissionId, createdDate, lastModifiedDate, createdBy, lastModifiedBy);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (roleName != null ? "roleName=" + roleName + ", " : "") +
                (permissionId != null ? "permissionId=" + permissionId + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            "}";
    }
}
