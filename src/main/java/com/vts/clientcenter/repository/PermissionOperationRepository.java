package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.PermissionOperation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PermissionOperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionOperationRepository
    extends JpaRepository<PermissionOperation, Long>, JpaSpecificationExecutor<PermissionOperation> {
    void deleteByRolePermissionIdIn(List<Long> ids);

    List<PermissionOperation> findByRolePermissionId(Long id);
}
