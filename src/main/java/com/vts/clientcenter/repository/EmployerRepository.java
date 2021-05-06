package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Employer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Employer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long>, JpaSpecificationExecutor<Employer> {
    Optional<Employer> getByName(String name);
}
