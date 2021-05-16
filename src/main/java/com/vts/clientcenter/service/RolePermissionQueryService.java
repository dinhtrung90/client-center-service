package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.domain.RolePermission;
import com.vts.clientcenter.repository.RolePermissionRepository;
import com.vts.clientcenter.service.dto.RolePermissionCriteria;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import com.vts.clientcenter.service.mapper.RolePermissionMapper;
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
 * Service for executing complex queries for {@link RolePermission} entities in the database.
 * The main input is a {@link RolePermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RolePermissionDTO} or a {@link Page} of {@link RolePermissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RolePermissionQueryService extends QueryService<RolePermission> {
    private final Logger log = LoggerFactory.getLogger(RolePermissionQueryService.class);

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionQueryService(RolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * Return a {@link List} of {@link RolePermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RolePermissionDTO> findByCriteria(RolePermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RolePermission> specification = createSpecification(criteria);
        return rolePermissionMapper.toDto(rolePermissionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RolePermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RolePermissionDTO> findByCriteria(RolePermissionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RolePermission> specification = createSpecification(criteria);
        return rolePermissionRepository.findAll(specification, page).map(rolePermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RolePermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RolePermission> specification = createSpecification(criteria);
        return rolePermissionRepository.count(specification);
    }

    /**
     * Function to convert {@link RolePermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RolePermission> createSpecification(RolePermissionCriteria criteria) {
        Specification<RolePermission> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RolePermission_.id));
            }
            if (criteria.getRoleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoleName(), RolePermission_.roleName));
            }
            if (criteria.getPermissionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPermissionId(), RolePermission_.permissionId));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), RolePermission_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), RolePermission_.lastModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), RolePermission_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), RolePermission_.lastModifiedBy));
            }
        }
        return specification;
    }
}
