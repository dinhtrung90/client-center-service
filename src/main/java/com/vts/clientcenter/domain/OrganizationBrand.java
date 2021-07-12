package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A OrganizationBrand.
 */
@Entity
@Table(name = "tv_organization_brand")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganizationBrand extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "background_color")
    private String backgroundColor;

    @Column(name = "is_primary")
    private boolean isPrimary;

    @Column(name = "sub_domain", unique = true)
    private String subDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "organizationBrands", allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public OrganizationBrand logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public OrganizationBrand primaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
        return this;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public OrganizationBrand backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Organization getOrganization() {
        return organization;
    }

    public OrganizationBrand organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationBrand)) {
            return false;
        }
        return id != null && id.equals(((OrganizationBrand) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationBrand{" +
            "id=" + getId() +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", primaryColor='" + getPrimaryColor() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            "}";
    }
}
