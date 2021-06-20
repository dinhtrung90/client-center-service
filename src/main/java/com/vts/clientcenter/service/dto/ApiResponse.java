package com.vts.clientcenter.service.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    private T response;
    private boolean isIsSuccess;
}
