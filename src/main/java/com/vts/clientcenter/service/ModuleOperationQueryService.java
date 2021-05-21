package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.domain.ModuleOperation;
import com.vts.clientcenter.repository.ModuleOperationRepository;
import com.vts.clientcenter.service.dto.ModuleOperationCriteria;
import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import com.vts.clientcenter.service.mapper.ModuleOperationMapper;
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
 * Service for executing complex queries for {@link ModuleOperation} entities in the database.
 * The main input is a {@link ModuleOperationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ModuleOperationDTO} or a {@link Page} of {@link ModuleOperationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModuleOperationQueryService extends QueryService<ModuleOperation> {
    private final Logger log = LoggerFactory.getLogger(ModuleOperationQueryService.class);

    private final ModuleOperationRepository moduleOperationRepository;

    private final ModuleOperationMapper moduleOperationMapper;

    public ModuleOperationQueryService(ModuleOperationRepository moduleOperationRepository, ModuleOperationMapper moduleOperationMapper) {
        this.moduleOperationRepository = moduleOperationRepository;
        this.moduleOperationMapper = moduleOperationMapper;
    }

    /**
     * Return a {@link List} of {@link ModuleOperationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ModuleOperationDTO> findByCriteria(ModuleOperationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ModuleOperation> specification = createSpecification(criteria);
        return moduleOperationMapper.toDto(moduleOperationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ModuleOperationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ModuleOperationDTO> findByCriteria(ModuleOperationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ModuleOperation> specification = createSpecification(criteria);
        return moduleOperationRepository.findAll(specification, page).map(moduleOperationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ModuleOperationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ModuleOperation> specification = createSpecification(criteria);
        return moduleOperationRepository.count(specification);
    }

    /**
     * Function to convert {@link ModuleOperationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ModuleOperation> createSpecification(ModuleOperationCriteria criteria) {
        Specification<ModuleOperation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ModuleOperation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildSpecification(criteria.getName(), ModuleOperation_.name));
            }
        }
        return specification;
    }
}
