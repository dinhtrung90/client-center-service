package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Employee;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface EmployeeExtensionRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByEmailAddress(String email);
}
