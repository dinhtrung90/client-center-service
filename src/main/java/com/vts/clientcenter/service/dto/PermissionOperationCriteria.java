package com.vts.clientcenter.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.PermissionOperation} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.PermissionOperationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /permission-operations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PermissionOperationCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter rolePermissionId;

    private LongFilter operationId;

    public PermissionOperationCriteria() {}

    public PermissionOperationCriteria(PermissionOperationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rolePermissionId = other.rolePermissionId == null ? null : other.rolePermissionId.copy();
        this.operationId = other.operationId == null ? null : other.operationId.copy();
    }

    @Override
    public PermissionOperationCriteria copy() {
        return new PermissionOperationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(LongFilter rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public LongFilter getOperationId() {
        return operationId;
    }

    public void setOperationId(LongFilter operationId) {
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PermissionOperationCriteria that = (PermissionOperationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rolePermissionId, that.rolePermissionId) &&
            Objects.equals(operationId, that.operationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rolePermissionId, operationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionOperationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rolePermissionId != null ? "rolePermissionId=" + rolePermissionId + ", " : "") +
                (operationId != null ? "operationId=" + operationId + ", " : "") +
            "}";
    }
}
