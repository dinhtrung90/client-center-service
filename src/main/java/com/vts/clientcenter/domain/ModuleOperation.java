package com.vts.clientcenter.domain;

import com.vts.clientcenter.domain.enumeration.OperationEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ModuleOperation.
 */
@Entity
@Table(name = "tv_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ModuleOperation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private OperationEnum name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "operations", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<RolePermission> rolePermissions;
}
