package com.vts.clientcenter.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginResponse {

    private String idToken;

    private String accessToken;
}
