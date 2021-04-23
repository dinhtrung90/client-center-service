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

import com.vts.clientcenter.domain.Employer;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.EmployerRepository;
import com.vts.clientcenter.service.dto.EmployerCriteria;
import com.vts.clientcenter.service.dto.EmployerDTO;
import com.vts.clientcenter.service.mapper.EmployerMapper;

/**
 * Service for executing complex queries for {@link Employer} entities in the database.
 * The main input is a {@link EmployerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployerDTO} or a {@link Page} of {@link EmployerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployerQueryService extends QueryService<Employer> {

    private final Logger log = LoggerFactory.getLogger(EmployerQueryService.class);

    private final EmployerRepository employerRepository;

    private final EmployerMapper employerMapper;

    public EmployerQueryService(EmployerRepository employerRepository, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.employerMapper = employerMapper;
    }

    /**
     * Return a {@link List} of {@link EmployerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployerDTO> findByCriteria(EmployerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employer> specification = createSpecification(criteria);
        return employerMapper.toDto(employerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployerDTO> findByCriteria(EmployerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employer> specification = createSpecification(criteria);
        return employerRepository.findAll(specification, page)
            .map(employerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employer> specification = createSpecification(criteria);
        return employerRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employer> createSpecification(EmployerCriteria criteria) {
        Specification<Employer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employer_.id));
            }
            if (criteria.getEmployerKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployerKey(), Employer_.employerKey));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Employer_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Employer_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Employer_.phone));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Employer_.address));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), Employer_.street));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Employer_.city));
            }
            if (criteria.getCounty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounty(), Employer_.county));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), Employer_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), Employer_.latitude));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Employer_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Employer_.lastModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Employer_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Employer_.lastModifiedBy));
            }
            if (criteria.getEmployerDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployerDepartmentId(),
                    root -> root.join(Employer_.employerDepartments, JoinType.LEFT).get(EmployerDepartment_.id)));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(Employer_.employees, JoinType.LEFT).get(Employee_.id)));
            }
            if (criteria.getEmployerBrandId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployerBrandId(),
                    root -> root.join(Employer_.employerBrands, JoinType.LEFT).get(EmployerBrand_.id)));
            }
        }
        return specification;
    }
}
