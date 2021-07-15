package com.vts.clientcenter.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.OrganizationBrand} entity.
 */
public class OrganizationBrandDTO implements Serializable {

    private Long id;

    private String logoUrl;

    private String primaryColor;

    private String backgroundColor;

    private boolean isPrimary;

    private String subDomain;

    private String organizationId;

    private String isEnabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

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

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationBrandDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationBrandDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationBrandDTO{" +
            "id=" + getId() +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", primaryColor='" + getPrimaryColor() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            ", organizationId=" + getOrganizationId() +
            "}";
    }
}
