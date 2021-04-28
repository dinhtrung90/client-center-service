package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.EmployerDepartment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployerDepartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerDepartmentRepository extends JpaRepository<EmployerDepartment, Long>, JpaSpecificationExecutor<EmployerDepartment> {
}
