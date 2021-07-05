package com.vts.clientcenter.web.rest.admin;

import com.vts.clientcenter.service.AccountService;
import com.vts.clientcenter.service.UserAddressService;
import com.vts.clientcenter.service.dto.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link com.vts.clientcenter.domain.User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/cms")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
public class AdminAccountResource {
    private final Logger log = LoggerFactory.getLogger(AdminAccountResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountService accountService;

    public AdminAccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public ResponseEntity<UserReferenceDto> createUser(@RequestBody CreateAccountRequest userDto) throws URISyntaxException {
        log.debug("REST request to create User : {}", userDto);
        UserReferenceDto referenceDto = accountService.createUserAccount(userDto);
        return ResponseEntity
            .created(new URI("/api/cms/account/get" + referenceDto.getUserId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "ACCOUNT", referenceDto.getUserId().toString()))
            .body(referenceDto);
    }

    @GetMapping("/account/get")
    public ResponseEntity<List<UserDTO>> getAllAccountInSystem(UserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get accounts by criteria: {}", criteria);
        Page<UserDTO> response = accountService.getAccounts(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), response);
        return new ResponseEntity<>(response.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<UserFullInfoResponse> getAccount(@RequestParam(name = "userId") String userId) {
        log.debug("REST request to get account : {}", userId);
        UserFullInfoResponse payload = accountService.getAccount(userId);
        return ResponseEntity.ok(payload);
    }

    @PutMapping("/account/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateAccountRequest userDto) {
        log.debug("REST request to create User : {}", userDto);
        UserDTO response = accountService.updateUserInfo(userDto);
        return ResponseEntity
            .ok(response);
    }

    @PostMapping("/account/{userId}/resend-verify-email")
    public ResponseEntity<ApiResponse> resendVerifyEmail(@PathVariable String userId) {
        log.debug("REST request to create User : {}", userId);
        ApiResponse response = accountService.resendVerifyEmail(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/account/{userId}/reset-password-email")
    public ResponseEntity<ApiResponse> resetPasswordUser(@PathVariable String userId) {
        log.debug("REST request to create User : {}", userId);
        ApiResponse response = accountService.resetPasswordUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @PostMapping("/account/{userId}/required-actions")
    public ResponseEntity<ApiResponse> createUser(@PathVariable String userId, @RequestBody List<String> actions) throws URISyntaxException {
        log.debug("REST request to set required actions : {}", actions);
        ApiResponse response = accountService.setRequiredActions(userId, actions);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/account/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        log.debug("REST request to delete user : {}", userId);
         accountService.removeAccountFromKeycloak(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/account/approve/{userId}")
    public ResponseEntity<UserFullInfoResponse> approvalAccount(@PathVariable String userId) {
        log.debug("REST request to create User : {}", userId);
        UserFullInfoResponse response = accountService.approveAccount(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{userId}/address")
    public ResponseEntity<List<UserAddressDTO>> getAllUserAddresses(@PathVariable String userId, Pageable pageable) {
        log.debug("REST request to get UserAddresses by criteria: {}", userId);
        Page<UserAddressDTO> page = accountService.getAddressesByUserId(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/account/{userId}/address/create")
    public ResponseEntity<UserAddressDTO> postAddress(@PathVariable String userId, @Valid @RequestBody UserAddressDTO addressDTO) throws URISyntaxException {
        log.debug("REST request to create UserAddresses by criteria: {}", userId);
        addressDTO.setUserId(userId);
        UserAddressDTO result = accountService.createUserAddress(addressDTO);
        return ResponseEntity.created(new URI("/api/cms/program-user-collection-progresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/account/{userId}/address/update")
    public ResponseEntity<UserAddressDTO> updateUserAddress(@PathVariable String userId, @Valid @RequestBody UserAddressDTO addressDTO) throws URISyntaxException {
        log.debug("REST request to create UserAddresses by criteria: {}", userId);
        UserAddressDTO result = accountService.updateUserAddress(userId, addressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/account/{userId}/address/get/{addressId}")
    public ResponseEntity<UserAddressDTO> getUserAddress(@PathVariable String userId, @PathVariable Long addressId) throws URISyntaxException {
        log.debug("REST request to get UserAddresses by criteria: {}", addressId);
        Optional<UserAddressDTO> result = accountService.getUserAddress(userId, addressId);
        return ResponseUtil.wrapOrNotFound(result);
    }

    @DeleteMapping("/account/{userId}/address/delete/{addressId}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable String userId, @PathVariable Long addressId) throws URISyntaxException {
        log.debug("REST request to get UserAddresses by criteria: {}", addressId);
        accountService.deleteUserAddress(userId, addressId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, addressId.toString())).build();
    }


    @PostMapping("/account/{userId}/role/mapping")
    public ResponseEntity<UserRoleMappingResponse> createRoleMapping(@PathVariable String userId, @Valid @RequestBody UserRoleMappingRequest request) throws URISyntaxException {
        log.debug("REST request to create UserMapping by criteria: {}", request);
        UserRoleMappingResponse result = accountService.createUserRoleMapping(userId, request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/account/{userId}/terminated/{isTerminated}")
    public ResponseEntity<Void> terminateAccount(@PathVariable String userId, @PathVariable String isTerminated) throws URISyntaxException {
        log.debug("REST request to terminate account by userId : {}", userId);
        accountService.terminateAccount(userId, Boolean.parseBoolean(isTerminated));
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, userId)).build();
    }


}
