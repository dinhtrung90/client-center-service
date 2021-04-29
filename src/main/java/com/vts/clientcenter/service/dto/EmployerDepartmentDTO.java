package com.vts.clientcenter.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.EmployerDepartment} entity.
 */
public class EmployerDepartmentDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String departmentName;

    private Instant createdDate;

    private Instant lstModifiedDate;

    private String createdBy;

    private String lastModifiedBy;


    private Long employerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLstModifiedDate() {
        return lstModifiedDate;
    }

    public void setLstModifiedDate(Instant lstModifiedDate) {
        this.lstModifiedDate = lstModifiedDate;
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

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployerDepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployerDepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployerDepartmentDTO{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lstModifiedDate='" + getLstModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", employerId=" + getEmployerId() +
            "}";
    }
}
