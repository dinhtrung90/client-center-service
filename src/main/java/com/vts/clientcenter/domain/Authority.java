package com.vts.clientcenter.domain;

import java.awt.print.Book;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "jhi_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Setter
@Getter
@ToString

public class Authority extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RolePermission> rolePermissions;

    @ManyToMany(mappedBy = "authorities", cascade = CascadeType.REMOVE)
    private Set<User> users;

    public void addUser(User user) {
        this.users.add(user);
        user.getAuthorities().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getAuthorities().remove(this);
    }

    public Authority addRolePermission(RolePermission rolePermission) {
        this.rolePermissions.add(rolePermission);
        rolePermission.setRole(this);
        return this;
    }

    public Authority removeRolePermission(RolePermission rolePermission) {
        this.rolePermissions.remove(rolePermission);
        rolePermission.setRole(null);
        return this;
    }
}
