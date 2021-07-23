package com.vts.clientcenter.service.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedPresentDto {

    @NonNull
    private String phoneOrEmployeeId;

    @NonNull
    private String code;
}
