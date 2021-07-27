package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.service.dto.UserDTO;

import java.util.List;

public interface UserSearchService {

    List<UserDTO> searchUserByKeyword(String query);

    List<UserDTO> processSearchNameByKeyword(String query);
}
