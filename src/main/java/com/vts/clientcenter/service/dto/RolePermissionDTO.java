package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.RolePermission} entity.
 */
public class RolePermissionDTO implements Serializable {
    private Long id;

    @NotNull
    private String roleName;

    @NotNull
    private String permission_id;

    private Boolean enableCreate;

    private Boolean enableUpdate;

    private Boolean enableRead;

    private Boolean enableDelete;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(String permission_id) {
        this.permission_id = permission_id;
    }

    public Boolean isEnableCreate() {
        return enableCreate;
    }

    public void setEnableCreate(Boolean enableCreate) {
        this.enableCreate = enableCreate;
    }

    public Boolean isEnableUpdate() {
        return enableUpdate;
    }

    public void setEnableUpdate(Boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    public Boolean isEnableRead() {
        return enableRead;
    }

    public void setEnableRead(Boolean enableRead) {
        this.enableRead = enableRead;
    }

    public Boolean isEnableDelete() {
        return enableDelete;
    }

    public void setEnableDelete(Boolean enableDelete) {
        this.enableDelete = enableDelete;
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
        if (!(o instanceof RolePermissionDTO)) {
            return false;
        }

        return id != null && id.equals(((RolePermissionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionDTO{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", permission_id='" + getPermission_id() + "'" +
            ", enableCreate='" + isEnableCreate() + "'" +
            ", enableUpdate='" + isEnableUpdate() + "'" +
            ", enableRead='" + isEnableRead() + "'" +
            ", enableDelete='" + isEnableDelete() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
