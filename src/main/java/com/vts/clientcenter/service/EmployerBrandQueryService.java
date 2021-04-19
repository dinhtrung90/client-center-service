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

import com.vts.clientcenter.domain.EmployerBrand;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.EmployerBrandRepository;
import com.vts.clientcenter.service.dto.EmployerBrandCriteria;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.mapper.EmployerBrandMapper;

/**
 * Service for executing complex queries for {@link EmployerBrand} entities in the database.
 * The main input is a {@link EmployerBrandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployerBrandDTO} or a {@link Page} of {@link EmployerBrandDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployerBrandQueryService extends QueryService<EmployerBrand> {

    private final Logger log = LoggerFactory.getLogger(EmployerBrandQueryService.class);

    private final EmployerBrandRepository employerBrandRepository;

    private final EmployerBrandMapper employerBrandMapper;

    public EmployerBrandQueryService(EmployerBrandRepository employerBrandRepository, EmployerBrandMapper employerBrandMapper) {
        this.employerBrandRepository = employerBrandRepository;
        this.employerBrandMapper = employerBrandMapper;
    }

    /**
     * Return a {@link List} of {@link EmployerBrandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployerBrandDTO> findByCriteria(EmployerBrandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployerBrand> specification = createSpecification(criteria);
        return employerBrandMapper.toDto(employerBrandRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployerBrandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployerBrandDTO> findByCriteria(EmployerBrandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployerBrand> specification = createSpecification(criteria);
        return employerBrandRepository.findAll(specification, page)
            .map(employerBrandMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployerBrandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployerBrand> specification = createSpecification(criteria);
        return employerBrandRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployerBrandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployerBrand> createSpecification(EmployerBrandCriteria criteria) {
        Specification<EmployerBrand> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployerBrand_.id));
            }
            if (criteria.getLogoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoUrl(), EmployerBrand_.logoUrl));
            }
            if (criteria.getPrimaryColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimaryColor(), EmployerBrand_.primaryColor));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), EmployerBrand_.displayName));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), EmployerBrand_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), EmployerBrand_.lastModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), EmployerBrand_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), EmployerBrand_.lastModifiedBy));
            }
            if (criteria.getEmployerId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployerId(),
                    root -> root.join(EmployerBrand_.employer, JoinType.LEFT).get(Employer_.id)));
            }
        }
        return specification;
    }
}
