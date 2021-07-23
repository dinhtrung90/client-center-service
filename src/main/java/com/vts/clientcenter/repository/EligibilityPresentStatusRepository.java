package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.EligibilityPresentStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EligibilityPresentStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EligibilityPresentStatusRepository extends JpaRepository<EligibilityPresentStatus, Long>, JpaSpecificationExecutor<EligibilityPresentStatus> {
}
