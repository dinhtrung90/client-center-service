package com.vts.clientcenter.service.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {
    private boolean success;
    private UserDTO userInfo;
}
