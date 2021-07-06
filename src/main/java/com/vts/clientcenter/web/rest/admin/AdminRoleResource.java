package com.vts.clientcenter.web.rest.admin;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.PermissionDetailDto;
import com.vts.clientcenter.service.dto.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
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
public class AdminRoleResource {
    private final Logger log = LoggerFactory.getLogger(AdminRoleResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityService authorityService;

    public AdminRoleResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @PostMapping("/role/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<RoleDetailResponse> createRole(@Valid @RequestBody CreateRoleRequest dto) throws URISyntaxException {
        log.debug("REST request to create authorityDto : {}", dto);
        RoleDetailResponse authorityDto = authorityService.save(dto);
        return ResponseEntity.ok(authorityDto);
    }

    @GetMapping("/role/get")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<AuthorityDto>> getAllRoles(Pageable pageable) {
        Page<AuthorityDto> response = authorityService.getAuthorities(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), response);
        return new ResponseEntity<>(response.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/role/permissions/get")
    public ResponseEntity<List<PermissionDetailDto>> getPermissions() {
        List<PermissionDetailDto> response = authorityService.getAllPermissions();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/role/getBy")
    public ResponseEntity<RoleDetailResponse> getRoleDetail(@RequestParam("name") String roleName) {
        RoleDetailResponse response = authorityService.getByRoleName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/role/update")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<RoleDetailResponse> updateRole(@Valid @RequestBody CreateRoleRequest dto) throws URISyntaxException {
        log.debug("REST request to update RoleRequest : {}", dto);
        RoleDetailResponse authorityDto = authorityService.save(dto);
        return ResponseEntity.ok(authorityDto);
    }
}
