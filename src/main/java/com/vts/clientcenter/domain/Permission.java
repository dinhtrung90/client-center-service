package com.vts.clientcenter.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

/**
 * A Permission.
 */
@Entity
@Table(name = "tv_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Permission  extends AbstractAuditingEntity implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        GrantedAuthority ga = (GrantedAuthority) o;
        return (getAuthority().equals(ga.getAuthority()));
    }

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public int hashCode() {
        return getAuthority().hashCode();
    }
}
