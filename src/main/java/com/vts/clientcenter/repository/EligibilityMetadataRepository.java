package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.EligibilityMetadata;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EligibilityMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EligibilityMetadataRepository extends JpaRepository<EligibilityMetadata, Long>, JpaSpecificationExecutor<EligibilityMetadata> {
}
