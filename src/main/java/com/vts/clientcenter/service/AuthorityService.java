package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.CreateRoleRequest;
import com.vts.clientcenter.service.dto.RoleDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorityService {

    Page<AuthorityDto> getAuthorities(Pageable pageable);

    RoleDetailResponse save(CreateRoleRequest dto);
}
