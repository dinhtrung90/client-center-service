package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Authority;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String>, JpaSpecificationExecutor<Authority> {
    //        @Query("select au from Authority au  left join fetch au.rolePermissions rp where au.name = :roleName")
    Optional<Authority> findByName(String roleName);

    Set<Authority> findAllByNameIn(List<String> roleNames);
}
