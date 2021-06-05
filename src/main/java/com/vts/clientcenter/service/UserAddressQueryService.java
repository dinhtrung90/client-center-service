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

import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.domain.*; // for static metamodels
import com.vts.clientcenter.repository.UserAddressRepository;
import com.vts.clientcenter.service.dto.UserAddressCriteria;
import com.vts.clientcenter.service.dto.UserAddressDTO;
import com.vts.clientcenter.service.mapper.UserAddressMapper;

/**
 * Service for executing complex queries for {@link UserAddress} entities in the database.
 * The main input is a {@link UserAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserAddressDTO} or a {@link Page} of {@link UserAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAddressQueryService extends QueryService<UserAddress> {

    private final Logger log = LoggerFactory.getLogger(UserAddressQueryService.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressQueryService(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * Return a {@link List} of {@link UserAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> findByCriteria(UserAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressMapper.toDto(userAddressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAddressDTO> findByCriteria(UserAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.findAll(specification, page)
            .map(userAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAddress> createSpecification(UserAddressCriteria criteria) {
        Specification<UserAddress> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserAddress_.id));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), UserAddress_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), UserAddress_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), UserAddress_.city));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), UserAddress_.country));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), UserAddress_.zipCode));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), UserAddress_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), UserAddress_.latitude));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserAddress_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), UserAddress_.lastModifiedDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), UserAddress_.createdBy));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), UserAddress_.lastModifiedBy));
            }
        }
        return specification;
    }
}
