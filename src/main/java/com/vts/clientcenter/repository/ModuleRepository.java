package com.vts.clientcenter.repository;

import com.vts.clientcenter.domain.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the {@link Module} entity.
 */
public interface ModuleRepository extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {

}

