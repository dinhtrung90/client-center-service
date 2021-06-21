package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.AuthorityDto;
import java.util.List;

public interface AuthorityService {

    List<AuthorityDto> getAuthorities();

    AuthorityDto save(AuthorityDto dto);
}
