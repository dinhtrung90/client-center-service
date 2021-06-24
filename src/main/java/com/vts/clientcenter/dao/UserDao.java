package com.vts.clientcenter.dao;

import com.vts.clientcenter.service.dto.UserAuthorizedResponseDto;
import com.vts.clientcenter.service.dto.UserDTO;

public interface UserDao {
    void handleRemoveFromRole(String role);

}
