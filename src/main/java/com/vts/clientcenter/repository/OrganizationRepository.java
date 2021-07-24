package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String>, JpaSpecificationExecutor<Organization> {

    boolean existsByName(String name);

        @Query("select o from Organization o left join fetch o.organizationBrands left join fetch o.organizationGroups where o.id = :uuid")
    Organization  getByUUID(@Param("uuid") String uuid);

    Optional<Organization> findByName(String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Organization a WHERE a.id = ?1")
    int deleteByIdentifier(String organizationId);

}
