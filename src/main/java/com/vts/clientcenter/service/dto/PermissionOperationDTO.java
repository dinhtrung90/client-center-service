package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.PermissionOperation} entity.
 */
public class PermissionOperationDTO implements Serializable {
    private Long id;

    @NotNull
    private Long rolePermissionId;

    @NotNull
    private Long operationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(Long rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionOperationDTO)) {
            return false;
        }

        return id != null && id.equals(((PermissionOperationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionOperationDTO{" +
            "id=" + getId() +
            ", rolePermissionId=" + getRolePermissionId() +
            ", operationId=" + getOperationId() +
            "}";
    }
}
