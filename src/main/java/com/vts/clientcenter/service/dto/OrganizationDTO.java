package com.vts.clientcenter.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.Organization} entity.
 */
public class OrganizationDTO implements Serializable {
    
    private Long id;

    @NotNull
    @Size(max = 50)
    private String organizationUUID;

    @NotNull
    private String name;

    private String displayName;

    private String description;

    private String email;

    private String phone;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationUUID() {
        return organizationUUID;
    }

    public void setOrganizationUUID(String organizationUUID) {
        this.organizationUUID = organizationUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationDTO{" +
            "id=" + getId() +
            ", organizationUUID='" + getOrganizationUUID() + "'" +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", description='" + getDescription() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
