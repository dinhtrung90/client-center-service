package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.ClientAppDto;
import com.vts.clientcenter.service.dto.CreateRoleRequest;
import com.vts.clientcenter.service.dto.RoleDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientApplicationService {

    List<ClientAppDto> getApplications();

}
