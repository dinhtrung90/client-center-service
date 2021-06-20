package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.Authority_;
import com.vts.clientcenter.repository.AuthorityRepository;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthorityQueryService extends QueryService<Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityQueryService.class);

    private final AuthorityRepository authorityRepository;


    public AuthorityQueryService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }


    @Transactional(readOnly = true)
    public PagingResponse<AuthorityDto> findByCriteria(AuthorityCriteria criteria, Pageable pageable) {

        log.debug("find by criteria : {}", criteria);

        final Specification<Authority> specification = createSpecification(criteria);

        Page<Authority> authorityList = authorityRepository.findAll(specification, pageable);
        PagingResponse<AuthorityDto> response = new PagingResponse<AuthorityDto>();
        response.setItems(authorityList.getContent().stream().map(
            r ->
                AuthorityDto
                    .builder()
                    .name(r.getName())
                    .description(r.getDescription())
                    .createdDate(r.getCreatedDate())
                    .lastModifiedDate(r.getLastModifiedDate())
                    .build()
        ).collect(Collectors.toList()));

        response.setTotalPage(authorityList.getTotalPages());
        response.setSize(pageable.getPageSize());

        return response;
    }


    protected Specification<Authority> createSpecification(AuthorityCriteria criteria) {
        Specification<Authority> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getRoleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoleName(), Authority_.name));
            }
        }
        return specification;
    }


}
