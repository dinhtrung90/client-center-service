package com.vts.clientcenter.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.OrganizationGroup} entity.
 */
public class OrganizationGroupDTO implements Serializable {

    private Long id;

    private String name;

    private String description;


    private String organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationGroupDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationGroupDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", organizationId=" + getOrganizationId() +
            "}";
    }
}
