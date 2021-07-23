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

import com.vts.clientcenter.domain.EligibilityPresentStatus;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.EligibilityPresentStatusRepository;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusCriteria;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;
import com.vts.clientcenter.service.mapper.EligibilityPresentStatusMapper;

/**
 * Service for executing complex queries for {@link EligibilityPresentStatus} entities in the database.
 * The main input is a {@link EligibilityPresentStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EligibilityPresentStatusDTO} or a {@link Page} of {@link EligibilityPresentStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EligibilityPresentStatusQueryService extends QueryService<EligibilityPresentStatus> {

    private final Logger log = LoggerFactory.getLogger(EligibilityPresentStatusQueryService.class);

    private final EligibilityPresentStatusRepository eligibilityPresentStatusRepository;

    private final EligibilityPresentStatusMapper eligibilityPresentStatusMapper;

    public EligibilityPresentStatusQueryService(EligibilityPresentStatusRepository eligibilityPresentStatusRepository, EligibilityPresentStatusMapper eligibilityPresentStatusMapper) {
        this.eligibilityPresentStatusRepository = eligibilityPresentStatusRepository;
        this.eligibilityPresentStatusMapper = eligibilityPresentStatusMapper;
    }

    /**
     * Return a {@link List} of {@link EligibilityPresentStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EligibilityPresentStatusDTO> findByCriteria(EligibilityPresentStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EligibilityPresentStatus> specification = createSpecification(criteria);
        return eligibilityPresentStatusMapper.toDto(eligibilityPresentStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EligibilityPresentStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EligibilityPresentStatusDTO> findByCriteria(EligibilityPresentStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EligibilityPresentStatus> specification = createSpecification(criteria);
        return eligibilityPresentStatusRepository.findAll(specification, page)
            .map(eligibilityPresentStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EligibilityPresentStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EligibilityPresentStatus> specification = createSpecification(criteria);
        return eligibilityPresentStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link EligibilityPresentStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EligibilityPresentStatus> createSpecification(EligibilityPresentStatusCriteria criteria) {
        Specification<EligibilityPresentStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EligibilityPresentStatus_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), EligibilityPresentStatus_.code));
            }
            if (criteria.getQrCodeUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQrCodeUrl(), EligibilityPresentStatus_.qrCodeUrl));
            }
            if (criteria.getHasPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getHasPresent(), EligibilityPresentStatus_.hasPresent));
            }
            if (criteria.getExpiredAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiredAt(), EligibilityPresentStatus_.expiredAt));
            }

        }
        return specification;
    }
}
