package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.EmployerBrand;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployerBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerBrandRepository extends JpaRepository<EmployerBrand, Long>, JpaSpecificationExecutor<EmployerBrand> {
}
