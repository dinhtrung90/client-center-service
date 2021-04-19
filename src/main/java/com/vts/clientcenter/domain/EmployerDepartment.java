package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A EmployerDepartment.
 */
@Entity
@Table(name = "tv_employer_department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmployerDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "lst_modified_date")
    private Instant lstModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "employerDepartments", allowSetters = true)
    private Employer employer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public EmployerDepartment departmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public EmployerDepartment createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLstModifiedDate() {
        return lstModifiedDate;
    }

    public EmployerDepartment lstModifiedDate(Instant lstModifiedDate) {
        this.lstModifiedDate = lstModifiedDate;
        return this;
    }

    public void setLstModifiedDate(Instant lstModifiedDate) {
        this.lstModifiedDate = lstModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public EmployerDepartment createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public EmployerDepartment lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Employer getEmployer() {
        return employer;
    }

    public EmployerDepartment employer(Employer employer) {
        this.employer = employer;
        return this;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployerDepartment)) {
            return false;
        }
        return id != null && id.equals(((EmployerDepartment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployerDepartment{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lstModifiedDate='" + getLstModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
