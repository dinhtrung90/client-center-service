package com.vts.clientcenter.web.rest.admin;

import com.vts.clientcenter.service.UserAddressQueryService;
import com.vts.clientcenter.service.UserAddressService;
import com.vts.clientcenter.service.dto.UserAddressCriteria;
import com.vts.clientcenter.service.dto.UserAddressDTO;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.UserAddress}.
 */
@RestController
@RequestMapping("/api/cms")
public class AdminUserAddressResource {

    private final Logger log = LoggerFactory.getLogger(AdminUserAddressResource.class);

    private final UserAddressService userAddressService;

    private final UserAddressQueryService userAddressQueryService;

    public AdminUserAddressResource(UserAddressService userAddressService, UserAddressQueryService userAddressQueryService) {
        this.userAddressService = userAddressService;
        this.userAddressQueryService = userAddressQueryService;
    }

    /**
     * {@code GET  /user-addresses} : get all the userAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAddresses in body.
     */
    @GetMapping("/user-addresses")
    public ResponseEntity<List<UserAddressDTO>> getAllUserAddresses(UserAddressCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserAddresses by criteria: {}", criteria);
        Page<UserAddressDTO> page = userAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-addresses/count} : count all the userAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-addresses/count")
    public ResponseEntity<Long> countUserAddresses(UserAddressCriteria criteria) {
        log.debug("REST request to count UserAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(userAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-addresses/:id} : get the "id" userAddress.
     *
     * @param id the id of the userAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-addresses/{id}")
    public ResponseEntity<UserAddressDTO> getUserAddress(@PathVariable Long id) {
        log.debug("REST request to get UserAddress : {}", id);
        Optional<UserAddressDTO> userAddressDTO = userAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAddressDTO);
    }
}
