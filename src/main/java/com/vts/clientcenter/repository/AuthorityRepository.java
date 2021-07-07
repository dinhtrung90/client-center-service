package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Authority;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String>, JpaSpecificationExecutor<Authority> {
    //        @Query("select au from Authority au  left join fetch au.rolePermissions rp where au.name = :roleName")
    Optional<Authority> findByName(String roleName);

    Set<Authority> findAllByNameIn(List<String> roleNames);

    Page<Authority> getAllByNameStartingWithAndNameIsNotContainingAndNameIsNotContaining(String role, String access, String permission ,Pageable pageable);

    @Query("select a from Authority a left join fetch a.permissions where a.name in :roles")
    Set<Authority> getAllByNameIn(@Param("roles") List<String> roleNames);
}
