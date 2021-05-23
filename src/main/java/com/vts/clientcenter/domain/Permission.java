package com.vts.clientcenter.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Permission.
 */
@Entity
@Table(name = "tv_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Permission extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<RolePermission> rolePermissions;


}
