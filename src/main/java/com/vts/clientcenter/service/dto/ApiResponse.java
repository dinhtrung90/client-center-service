package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    private T response;
    private boolean isIsSuccess;
    private String message;
    private HttpStatus statusCode;
}
