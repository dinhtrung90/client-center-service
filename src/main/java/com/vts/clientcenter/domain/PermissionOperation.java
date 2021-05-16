package com.vts.clientcenter.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PermissionOperation.
 */
@Entity
@Table(name = "tv_permission_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PermissionOperation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_permission_id", nullable = false)
    private Long rolePermissionId;

    @NotNull
    @Column(name = "operation_id", nullable = false)
    private Long operationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRolePermissionId() {
        return rolePermissionId;
    }

    public PermissionOperation rolePermissionId(Long rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
        return this;
    }

    public void setRolePermissionId(Long rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public PermissionOperation operationId(Long operationId) {
        this.operationId = operationId;
        return this;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionOperation)) {
            return false;
        }
        return id != null && id.equals(((PermissionOperation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionOperation{" +
            "id=" + getId() +
            ", rolePermissionId=" + getRolePermissionId() +
            ", operationId=" + getOperationId() +
            "}";
    }
}
