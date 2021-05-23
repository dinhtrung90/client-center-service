package com.vts.clientcenter.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;

/**
 * A RolePermission.
 */
@Entity
@Table(name = "tv_role_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @NotNull
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_name", insertable = false, updatable = false)
    private Authority role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "tv_permission_operation",
        joinColumns = @JoinColumn(name = "role_permission_id"),
        inverseJoinColumns = @JoinColumn(name = "operation_id"))
    private Set<ModuleOperation> operations;
}
