package com.vts.clientcenter.repository;

import com.sun.tools.javac.util.List;
import com.vts.clientcenter.domain.Organization;

import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.domain.UserOrganizationMembership;
import org.springframework.data.jpa.repository.*;
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

    @Query("select u.user from UserOrganizationMembership u" +
        " left join fetch u.organization" +
        " where u.organization.id = :organizationId and u.user.id = :userId")
    Optional<User> getUserMembersInOrganization(@Param("organizationId") String id, @Param("userId") String userId);

}
