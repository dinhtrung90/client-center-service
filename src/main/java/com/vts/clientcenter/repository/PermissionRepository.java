package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Permission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {}
