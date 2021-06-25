package com.vts.clientcenter.domain;

import java.awt.print.Book;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.CollectionUtils;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "jhi_authority")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tv_role_permission",
        joinColumns = { @JoinColumn(name = "role_name") },
        inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    public Set<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(name = "tv_composite_role", joinColumns = @JoinColumn(name = "composite"), inverseJoinColumns = @JoinColumn(name = "child_role"))
    private Set<Authority> compositeRoles;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    public Set<Authority> getCompositeRoles() {
        return compositeRoles;
    }

    public void addCompositeRoles(Set<Authority> compositeRoles) {
        if (!CollectionUtils.isEmpty(compositeRoles)) {
            this.compositeRoles.clear();
        }
        this.compositeRoles = compositeRoles;
    }


}
