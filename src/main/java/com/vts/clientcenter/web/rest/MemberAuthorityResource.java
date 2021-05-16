package com.vts.clientcenter.web.rest;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.RolePermissionExtensionService;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.EditPermissionRequestDto;
import com.vts.clientcenter.service.dto.EditPermissionResponseDto;
import io.github.jhipster.web.util.HeaderUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Authority}.
 */
@RestController
@RequestMapping("/api")
public class MemberAuthorityResource {
    private final Logger log = LoggerFactory.getLogger(MemberAuthorityResource.class);

    private final AuthorityService authorityService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolePermissionExtensionService rolePermissionExtensionService;

    public MemberAuthorityResource(AuthorityService authorityService, RolePermissionExtensionService rolePermissionExtensionService) {
        this.authorityService = authorityService;
        this.rolePermissionExtensionService = rolePermissionExtensionService;
    }

    @PostMapping("/member-roles/create-roles")
    public ResponseEntity<AuthorityDto> createRoles(@Valid @RequestBody AuthorityDto dto) throws URISyntaxException {
        log.debug("request create role: {}", dto);
        AuthorityDto result = authorityService.save(dto);
        return ResponseEntity
            .created(new URI("/api/permissions/" + result.getRoleName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRoleName()))
            .body(result);
    }

    @GetMapping("/member-roles/get-roles")
    public ResponseEntity<List<AuthorityDto>> getAllRolesInSystem() {
        log.debug("REST request to get roles");

        List<AuthorityDto> authorities = authorityService.getAuthorities();

        return ResponseEntity.ok().body(authorities);
    }

    @PostMapping("/member-roles/edit")
    public ResponseEntity<EditPermissionResponseDto> setPermission(@Valid @RequestBody EditPermissionRequestDto dto) {
        log.debug("REST request to edit roles");

        EditPermissionResponseDto responseDto = rolePermissionExtensionService.saveDetail(dto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/member-roles")
    public ResponseEntity<EditPermissionResponseDto> getDetailRoles(@RequestParam(name = "roleName") String roleName) {
        log.debug("REST request to get detail role: {}", roleName);

        EditPermissionResponseDto responseDto = rolePermissionExtensionService.getDetailRole(roleName);

        return ResponseEntity.ok().body(responseDto);
    }
}
