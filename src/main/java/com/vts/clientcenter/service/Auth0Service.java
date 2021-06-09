package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.LoginResponse;

public interface Auth0Service {

    void createGroup();
    void createRole();

    LoginResponse login(String username, String password);
}
