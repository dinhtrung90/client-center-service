package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String>, JpaSpecificationExecutor<Authority> {}
