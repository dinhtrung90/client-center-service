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

import com.vts.clientcenter.domain.OrganizationGroup;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.OrganizationGroupRepository;
import com.vts.clientcenter.service.dto.OrganizationGroupCriteria;
import com.vts.clientcenter.service.dto.OrganizationGroupDTO;
import com.vts.clientcenter.service.mapper.OrganizationGroupMapper;

/**
 * Service for executing complex queries for {@link OrganizationGroup} entities in the database.
 * The main input is a {@link OrganizationGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationGroupDTO} or a {@link Page} of {@link OrganizationGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationGroupQueryService extends QueryService<OrganizationGroup> {

    private final Logger log = LoggerFactory.getLogger(OrganizationGroupQueryService.class);

    private final OrganizationGroupRepository organizationGroupRepository;

    private final OrganizationGroupMapper organizationGroupMapper;

    public OrganizationGroupQueryService(OrganizationGroupRepository organizationGroupRepository, OrganizationGroupMapper organizationGroupMapper) {
        this.organizationGroupRepository = organizationGroupRepository;
        this.organizationGroupMapper = organizationGroupMapper;
    }

    /**
     * Return a {@link List} of {@link OrganizationGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationGroupDTO> findByCriteria(OrganizationGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganizationGroup> specification = createSpecification(criteria);
        return organizationGroupMapper.toDto(organizationGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganizationGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationGroupDTO> findByCriteria(OrganizationGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganizationGroup> specification = createSpecification(criteria);
        return organizationGroupRepository.findAll(specification, page)
            .map(organizationGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganizationGroup> specification = createSpecification(criteria);
        return organizationGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationGroup> createSpecification(OrganizationGroupCriteria criteria) {
        Specification<OrganizationGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrganizationGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrganizationGroup_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OrganizationGroup_.description));
            }
            if (criteria.getOrganizationId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrganizationId(),
                    root -> root.join(OrganizationGroup_.organization, JoinType.LEFT).get(Organization_.id)));
            }
        }
        return specification;
    }
}
