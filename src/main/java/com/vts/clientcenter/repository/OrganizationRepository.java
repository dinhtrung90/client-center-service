package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Organization;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    boolean existsByName(String name);

    Optional<Organization> findByOrganizationUUID(String uuid);

    @Query("select o from Organization o left join fetch o.organizationBrands left join fetch o.organizationGroups where o.organizationUUID = :uuid")
    Organization  getByUUID(@Param("uuid") String uuid);
}