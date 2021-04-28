package com.vts.clientcenter.service.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TResult<T> {
    private  T result;
    private Boolean isSuccess;
    private String message;
    private Error error;
}
