package com.vts.clientcenter.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.vts.clientcenter.domain.EmployerDepartment;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.EmployerDepartmentRepository;
import com.vts.clientcenter.service.dto.EmployerDepartmentCriteria;
import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;
import com.vts.clientcenter.service.mapper.EmployerDepartmentMapper;

/**
 * Service for executing complex queries for {@link EmployerDepartment} entities in the database.
 * The main input is a {@link EmployerDepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployerDepartmentDTO} or a {@link Page} of {@link EmployerDepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployerDepartmentQueryService extends QueryService<EmployerDepartment> {

    private final Logger log = LoggerFactory.getLogger(EmployerDepartmentQueryService.class);

    private final EmployerDepartmentRepository employerDepartmentRepository;

    private final EmployerDepartmentMapper employerDepartmentMapper;

    public EmployerDepartmentQueryService(EmployerDepartmentRepository employerDepartmentRepository, EmployerDepartmentMapper employerDepartmentMapper) {
        this.employerDepartmentRepository = employerDepartmentRepository;
        this.employerDepartmentMapper = employerDepartmentMapper;
    }

    /**
     * Return a {@link List} of {@link EmployerDepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployerDepartmentDTO> findByCriteria(EmployerDepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployerDepartment> specification = createSpecification(criteria);
        return employerDepartmentMapper.toDto(employerDepartmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployerDepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployerDepartmentDTO> findByCriteria(EmployerDepartmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployerDepartment> specification = createSpecification(criteria);
        return employerDepartmentRepository.findAll(specification, page)
            .map(employerDepartmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployerDepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployerDepartment> specification = createSpecification(criteria);
        return employerDepartmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployerDepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployerDepartment> createSpecification(EmployerDepartmentCriteria criteria) {
        Specification<EmployerDepartment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployerDepartment_.id));
            }
            if (criteria.getDepartmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartmentName(), EmployerDepartment_.departmentName));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), EmployerDepartment_.createdDate));
            }
            if (criteria.getLstModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLstModifiedDate(), EmployerDepartment_.lstModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), EmployerDepartment_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), EmployerDepartment_.lastModifiedBy));
            }
            if (criteria.getEmployerId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployerId(),
                    root -> root.join(EmployerDepartment_.employer, JoinType.LEFT).get(Employer_.id)));
            }
        }
        return specification;
    }
}
