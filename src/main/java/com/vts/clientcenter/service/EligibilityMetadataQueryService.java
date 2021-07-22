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

import com.vts.clientcenter.domain.EligibilityMetadata;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.EligibilityMetadataRepository;
import com.vts.clientcenter.service.dto.EligibilityMetadataCriteria;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;
import com.vts.clientcenter.service.mapper.EligibilityMetadataMapper;

/**
 * Service for executing complex queries for {@link EligibilityMetadata} entities in the database.
 * The main input is a {@link EligibilityMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EligibilityMetadataDTO} or a {@link Page} of {@link EligibilityMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EligibilityMetadataQueryService extends QueryService<EligibilityMetadata> {

    private final Logger log = LoggerFactory.getLogger(EligibilityMetadataQueryService.class);

    private final EligibilityMetadataRepository eligibilityMetadataRepository;

    private final EligibilityMetadataMapper eligibilityMetadataMapper;

    public EligibilityMetadataQueryService(EligibilityMetadataRepository eligibilityMetadataRepository, EligibilityMetadataMapper eligibilityMetadataMapper) {
        this.eligibilityMetadataRepository = eligibilityMetadataRepository;
        this.eligibilityMetadataMapper = eligibilityMetadataMapper;
    }

    /**
     * Return a {@link List} of {@link EligibilityMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EligibilityMetadataDTO> findByCriteria(EligibilityMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EligibilityMetadata> specification = createSpecification(criteria);
        return eligibilityMetadataMapper.toDto(eligibilityMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EligibilityMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EligibilityMetadataDTO> findByCriteria(EligibilityMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EligibilityMetadata> specification = createSpecification(criteria);
        return eligibilityMetadataRepository.findAll(specification, page)
            .map(eligibilityMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EligibilityMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EligibilityMetadata> specification = createSpecification(criteria);
        return eligibilityMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link EligibilityMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EligibilityMetadata> createSpecification(EligibilityMetadataCriteria criteria) {
        Specification<EligibilityMetadata> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), EligibilityMetadata_.id));
            }
            if (criteria.getThumbUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbUrl(), EligibilityMetadata_.thumbUrl));
            }
        }
        return specification;
    }
}
