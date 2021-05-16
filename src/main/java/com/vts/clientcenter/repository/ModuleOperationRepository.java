package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.ModuleOperation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ModuleOperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleOperationRepository extends JpaRepository<ModuleOperation, Long>, JpaSpecificationExecutor<ModuleOperation> {}
