package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vts.clientcenter.config.Constants;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "tv_organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@NamedEntityGraph(name = "organization_userAddresses_graph",
    attributeNodes = {
        @NamedAttributeNode("userAddresses")
    }
)
public class Organization extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "name", nullable = false)
    @Pattern(regexp = Constants.LOGIN_REGEX)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<UserAddress> userAddresses = new HashSet<>();

    @OneToMany(mappedBy = "organization",  fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<OrganizationBrand> organizationBrands = new HashSet<>();

    @OneToMany(mappedBy = "organization",  fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<OrganizationGroup> organizationGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<UserOrganizationMembership> organizationUserMappings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Organization displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public Organization description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public Organization email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Organization phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public Organization userAddresses(Set<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
        return this;
    }

    public Organization addUserAddress(UserAddress userAddress) {
        this.userAddresses.add(userAddress);
        userAddress.setOrganization(this);
        return this;
    }

    public Organization removeUserAddress(UserAddress userAddress) {
        this.userAddresses.remove(userAddress);
        userAddress.setOrganization(null);
        return this;
    }

    public void setUserAddresses(Set<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public Set<OrganizationBrand> getOrganizationBrands() {
        return organizationBrands;
    }

    public Organization organizationBrands(Set<OrganizationBrand> organizationBrands) {
        this.organizationBrands = organizationBrands;
        return this;
    }

    public Organization addOrganizationBrand(OrganizationBrand organizationBrand) {
        this.organizationBrands.add(organizationBrand);
        organizationBrand.setOrganization(this);
        return this;
    }

    public Organization removeOrganizationBrand(OrganizationBrand organizationBrand) {
        this.organizationBrands.remove(organizationBrand);
        organizationBrand.setOrganization(null);
        return this;
    }

    public void setOrganizationBrands(Set<OrganizationBrand> organizationBrands) {
        this.organizationBrands = organizationBrands;
    }

    public Set<OrganizationGroup> getOrganizationGroups() {
        return organizationGroups;
    }

    public Organization organizationGroups(Set<OrganizationGroup> organizationGroups) {
        this.organizationGroups = organizationGroups;
        return this;
    }

    public Organization addOrganizationGroup(OrganizationGroup organizationGroup) {
        this.organizationGroups.add(organizationGroup);
        organizationGroup.setOrganization(this);
        return this;
    }

    public Organization removeOrganizationGroup(OrganizationGroup organizationGroup) {
        this.organizationGroups.remove(organizationGroup);
        organizationGroup.setOrganization(null);
        return this;
    }

    public void setOrganizationGroups(Set<OrganizationGroup> organizationGroups) {
        this.organizationGroups = organizationGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public Set<UserOrganizationMembership> getOrganizationUserMappings() {
        return organizationUserMappings;
    }

    public void setOrganizationUserMappings(Set<UserOrganizationMembership> organizationUserMappings) {
        this.organizationUserMappings = organizationUserMappings;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", description='" + getDescription() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
