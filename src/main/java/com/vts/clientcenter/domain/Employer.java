package com.vts.clientcenter.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employer.
 */
@Entity
@Table(name = "tv_employer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "employer_key", length = 100, nullable = false)
    private String employerKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "county")
    private String county;

    @NotNull
    @Size(max = 100)
    @Column(name = "longitude", length = 100, nullable = false)
    private String longitude;

    @NotNull
    @Size(max = 100)
    @Column(name = "latitude", length = 100, nullable = false)
    private String latitude;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @OneToMany(mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployerDepartment> employerDepartments = new HashSet<>();

    @OneToMany(mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployerBrand> employerBrands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployerKey() {
        return employerKey;
    }

    public Employer employerKey(String employerKey) {
        this.employerKey = employerKey;
        return this;
    }

    public void setEmployerKey(String employerKey) {
        this.employerKey = employerKey;
    }

    public String getName() {
        return name;
    }

    public Employer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Employer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Employer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Employer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public Employer street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public Employer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public Employer county(String county) {
        this.county = county;
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLongitude() {
        return longitude;
    }

    public Employer longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Employer latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Employer createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Employer lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Employer createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Employer lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<EmployerDepartment> getEmployerDepartments() {
        return employerDepartments;
    }

    public Employer employerDepartments(Set<EmployerDepartment> employerDepartments) {
        this.employerDepartments = employerDepartments;
        return this;
    }

    public Employer addEmployerDepartment(EmployerDepartment employerDepartment) {
        this.employerDepartments.add(employerDepartment);
        employerDepartment.setEmployer(this);
        return this;
    }

    public Employer removeEmployerDepartment(EmployerDepartment employerDepartment) {
        this.employerDepartments.remove(employerDepartment);
        employerDepartment.setEmployer(null);
        return this;
    }

    public void setEmployerDepartments(Set<EmployerDepartment> employerDepartments) {
        this.employerDepartments = employerDepartments;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Employer employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Employer addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setEmployer(this);
        return this;
    }

    public Employer removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setEmployer(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<EmployerBrand> getEmployerBrands() {
        return employerBrands;
    }

    public Employer employerBrands(Set<EmployerBrand> employerBrands) {
        this.employerBrands = employerBrands;
        return this;
    }

    public Employer addEmployerBrand(EmployerBrand employerBrand) {
        this.employerBrands.add(employerBrand);
        employerBrand.setEmployer(this);
        return this;
    }

    public Employer removeEmployerBrand(EmployerBrand employerBrand) {
        this.employerBrands.remove(employerBrand);
        employerBrand.setEmployer(null);
        return this;
    }

    public void setEmployerBrands(Set<EmployerBrand> employerBrands) {
        this.employerBrands = employerBrands;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employer)) {
            return false;
        }
        return id != null && id.equals(((Employer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employer{" +
            "id=" + getId() +
            ", employerKey='" + getEmployerKey() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", county='" + getCounty() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
