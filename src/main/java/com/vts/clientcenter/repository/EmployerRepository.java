package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Employer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long>, JpaSpecificationExecutor<Employer> {

    Optional<Employer>getByName(String name);
}
