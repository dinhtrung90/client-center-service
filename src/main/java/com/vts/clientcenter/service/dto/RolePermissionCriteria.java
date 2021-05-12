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

    private StringFilter permission_id;

    private BooleanFilter enableCreate;

    private BooleanFilter enableUpdate;

    private BooleanFilter enableRead;

    private BooleanFilter enableDelete;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    public RolePermissionCriteria() {}

    public RolePermissionCriteria(RolePermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.roleName = other.roleName == null ? null : other.roleName.copy();
        this.permission_id = other.permission_id == null ? null : other.permission_id.copy();
        this.enableCreate = other.enableCreate == null ? null : other.enableCreate.copy();
        this.enableUpdate = other.enableUpdate == null ? null : other.enableUpdate.copy();
        this.enableRead = other.enableRead == null ? null : other.enableRead.copy();
        this.enableDelete = other.enableDelete == null ? null : other.enableDelete.copy();
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

    public StringFilter getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(StringFilter permission_id) {
        this.permission_id = permission_id;
    }

    public BooleanFilter getEnableCreate() {
        return enableCreate;
    }

    public void setEnableCreate(BooleanFilter enableCreate) {
        this.enableCreate = enableCreate;
    }

    public BooleanFilter getEnableUpdate() {
        return enableUpdate;
    }

    public void setEnableUpdate(BooleanFilter enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    public BooleanFilter getEnableRead() {
        return enableRead;
    }

    public void setEnableRead(BooleanFilter enableRead) {
        this.enableRead = enableRead;
    }

    public BooleanFilter getEnableDelete() {
        return enableDelete;
    }

    public void setEnableDelete(BooleanFilter enableDelete) {
        this.enableDelete = enableDelete;
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
            Objects.equals(permission_id, that.permission_id) &&
            Objects.equals(enableCreate, that.enableCreate) &&
            Objects.equals(enableUpdate, that.enableUpdate) &&
            Objects.equals(enableRead, that.enableRead) &&
            Objects.equals(enableDelete, that.enableDelete) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            roleName,
            permission_id,
            enableCreate,
            enableUpdate,
            enableRead,
            enableDelete,
            createdDate,
            lastModifiedDate,
            createdBy,
            lastModifiedBy
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (roleName != null ? "roleName=" + roleName + ", " : "") +
                (permission_id != null ? "permission_id=" + permission_id + ", " : "") +
                (enableCreate != null ? "enableCreate=" + enableCreate + ", " : "") +
                (enableUpdate != null ? "enableUpdate=" + enableUpdate + ", " : "") +
                (enableRead != null ? "enableRead=" + enableRead + ", " : "") +
                (enableDelete != null ? "enableDelete=" + enableDelete + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            "}";
    }
}
