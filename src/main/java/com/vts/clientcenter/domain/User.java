package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.okta.sdk.resource.user.UserStatus;
import com.vts.clientcenter.config.Constants;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.vts.clientcenter.domain.enumeration.AccountStatus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Column(name = "has_verified_email")
    private boolean hasVerifiedEmail;

    @Column(name = "has_enabled")
    private boolean hasEnabled;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddresses= new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserProfile userProfile;

    @Column(name = "is_approved")
    private boolean isApproved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(Set<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public void addUserAddress(UserAddress userAddresses) {
        this.userAddresses.add(userAddresses);
        userAddresses.setUser(this);
    }

    public User removeUserAddress(UserAddress userAddresses) {
        this.userAddresses.remove(userAddresses);
        userAddresses.setUser(null);
        return this;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getUsers().add(this);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getUsers().remove(this);
    }

    public void removeAuthorities() {
        Iterator<Authority> iterator = this.authorities.iterator();

        while (iterator.hasNext()) {
            Authority authority = iterator.next();

            authority.getUsers().remove(this);
            iterator.remove();
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean hasVerifiedEmail() {
        return hasVerifiedEmail;
    }

    public void setHasVerifiedEmail(boolean hasVerifiedEmail) {
        this.hasVerifiedEmail = hasVerifiedEmail;
    }

    public boolean hasEnabled() {
        return hasEnabled;
    }

    public void setHasEnabled(boolean hasEnabled) {
        this.hasEnabled = hasEnabled;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void addAuthorities(List<Authority> assignRoles)  {
        this.authorities.clear();
        assignRoles.forEach(this::addAuthority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }


    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", langKey='" + langKey + '\'' +
            "}";
    }
}
