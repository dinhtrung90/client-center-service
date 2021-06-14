package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.UserDTO;

public interface IdentityService {

    UserDTO createUser(UserDTO userDto) throws Exception;
}
