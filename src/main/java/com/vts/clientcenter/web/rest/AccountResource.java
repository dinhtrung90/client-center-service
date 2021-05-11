package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.security.AuthoritiesConstants;
import com.vts.clientcenter.service.AccountService;
import com.vts.clientcenter.service.dto.ActivatedPayload;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.dto.UserDTO;
import java.security.Principal;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api")
public class AccountResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/account/create", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDTO> createAccount(@Valid @RequestBody UserDTO dto) throws Exception {
        log.debug("REST request to create account : {}", dto);
        UserDTO userAccount = accountService.createUserAccount(dto);
        return ResponseEntity.ok(userAccount);
    }

    @RequestMapping(value = "/account/update", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateAccount(@Valid @RequestBody UserDTO dto) throws Exception {
        log.debug("REST request to create account : {}", dto);
        UserDTO userAccount = accountService.updateAccount(dto);
        return ResponseEntity.ok(userAccount);
    }

    @RequestMapping(value = "/account/activate", method = RequestMethod.GET)
    public ResponseEntity<ActivatedPayload> activeAccount(@RequestParam(value = "key") String key) throws Exception {
        log.debug("REST request to activate account : {}", key);
        ActivatedPayload payload = accountService.activateAccount(key);
        return ResponseEntity.ok(payload);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getAccount(@RequestParam(name = "userId") String userId) {
        log.debug("REST request to get account : {}", userId);
        UserDTO payload = accountService.getAccount(userId);
        return ResponseEntity.ok(payload);
    }
}
