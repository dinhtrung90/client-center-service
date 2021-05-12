package com.vts.clientcenter.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RolePermission.
 */
@Entity
@Table(name = "tv_role_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @NotNull
    @Column(name = "permission_id", nullable = false)
    private String permission_id;

    @Column(name = "enable_create")
    private Boolean enableCreate;

    @Column(name = "enable_update")
    private Boolean enableUpdate;

    @Column(name = "enable_read")
    private Boolean enableRead;

    @Column(name = "enable_delete")
    private Boolean enableDelete;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public RolePermission roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermission_id() {
        return permission_id;
    }

    public RolePermission permission_id(String permission_id) {
        this.permission_id = permission_id;
        return this;
    }

    public void setPermission_id(String permission_id) {
        this.permission_id = permission_id;
    }

    public Boolean isEnableCreate() {
        return enableCreate;
    }

    public RolePermission enableCreate(Boolean enableCreate) {
        this.enableCreate = enableCreate;
        return this;
    }

    public void setEnableCreate(Boolean enableCreate) {
        this.enableCreate = enableCreate;
    }

    public Boolean isEnableUpdate() {
        return enableUpdate;
    }

    public RolePermission enableUpdate(Boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
        return this;
    }

    public void setEnableUpdate(Boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    public Boolean isEnableRead() {
        return enableRead;
    }

    public RolePermission enableRead(Boolean enableRead) {
        this.enableRead = enableRead;
        return this;
    }

    public void setEnableRead(Boolean enableRead) {
        this.enableRead = enableRead;
    }

    public Boolean isEnableDelete() {
        return enableDelete;
    }

    public RolePermission enableDelete(Boolean enableDelete) {
        this.enableDelete = enableDelete;
        return this;
    }

    public void setEnableDelete(Boolean enableDelete) {
        this.enableDelete = enableDelete;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public RolePermission createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public RolePermission lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RolePermission createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public RolePermission lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermission)) {
            return false;
        }
        return id != null && id.equals(((RolePermission) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermission{" +
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
