package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.OrganizationGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OrganizationGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationGroupRepository extends JpaRepository<OrganizationGroup, Long>, JpaSpecificationExecutor<OrganizationGroup> {
}
