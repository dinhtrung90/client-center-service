package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Eligibility;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Eligibility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, Long>, JpaSpecificationExecutor<Eligibility> {
}
