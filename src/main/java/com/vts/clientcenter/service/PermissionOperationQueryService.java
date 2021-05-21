package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.domain.PermissionOperation;
import com.vts.clientcenter.repository.PermissionOperationRepository;
import com.vts.clientcenter.service.dto.PermissionOperationCriteria;
import com.vts.clientcenter.service.dto.PermissionOperationDTO;
import com.vts.clientcenter.service.mapper.PermissionOperationMapper;
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
 * Service for executing complex queries for {@link PermissionOperation} entities in the database.
 * The main input is a {@link PermissionOperationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PermissionOperationDTO} or a {@link Page} of {@link PermissionOperationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionOperationQueryService extends QueryService<PermissionOperation> {
    private final Logger log = LoggerFactory.getLogger(PermissionOperationQueryService.class);

    private final PermissionOperationRepository permissionOperationRepository;

    private final PermissionOperationMapper permissionOperationMapper;

    public PermissionOperationQueryService(
        PermissionOperationRepository permissionOperationRepository,
        PermissionOperationMapper permissionOperationMapper
    ) {
        this.permissionOperationRepository = permissionOperationRepository;
        this.permissionOperationMapper = permissionOperationMapper;
    }

    /**
     * Return a {@link List} of {@link PermissionOperationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionOperationDTO> findByCriteria(PermissionOperationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PermissionOperation> specification = createSpecification(criteria);
        return permissionOperationMapper.toDto(permissionOperationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PermissionOperationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionOperationDTO> findByCriteria(PermissionOperationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PermissionOperation> specification = createSpecification(criteria);
        return permissionOperationRepository.findAll(specification, page).map(permissionOperationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionOperationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PermissionOperation> specification = createSpecification(criteria);
        return permissionOperationRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionOperationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PermissionOperation> createSpecification(PermissionOperationCriteria criteria) {
        Specification<PermissionOperation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PermissionOperation_.id));
            }
            if (criteria.getRolePermissionId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRolePermissionId(), PermissionOperation_.rolePermissionId));
            }
            if (criteria.getOperationId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOperationId(), PermissionOperation_.operationId));
            }
        }
        return specification;
    }
}
