package com.vts.clientcenter.service.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendVerifyEmailResponse {
    private String userId;
    private String email;
}
