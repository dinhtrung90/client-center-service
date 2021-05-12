package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.domain.Permission;
import com.vts.clientcenter.repository.PermissionRepository;
import com.vts.clientcenter.service.dto.PermissionCriteria;
import com.vts.clientcenter.service.dto.PermissionDTO;
import com.vts.clientcenter.service.mapper.PermissionMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Permission} entities in the database.
 * The main input is a {@link PermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PermissionDTO} or a {@link Page} of {@link PermissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionQueryService extends QueryService<Permission> {
    private final Logger log = LoggerFactory.getLogger(PermissionQueryService.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionQueryService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Return a {@link List} of {@link PermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findByCriteria(PermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionMapper.toDto(permissionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionDTO> findByCriteria(PermissionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionRepository.findAll(specification, page).map(permissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Permission> createSpecification(PermissionCriteria criteria) {
        Specification<Permission> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Permission_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Permission_.name));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Permission_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Permission_.lastModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Permission_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Permission_.lastModifiedBy));
            }
        }
        return specification;
    }
}
