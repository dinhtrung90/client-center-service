package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.OrganizationBrand;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OrganizationBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationBrandRepository extends JpaRepository<OrganizationBrand, Long>, JpaSpecificationExecutor<OrganizationBrand> {
}
