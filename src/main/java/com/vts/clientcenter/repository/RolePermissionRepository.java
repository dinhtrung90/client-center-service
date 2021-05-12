package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.RolePermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RolePermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {}
