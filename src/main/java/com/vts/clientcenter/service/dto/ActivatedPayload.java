package com.vts.clientcenter.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ActivatedPayload {
    private String userId;
    private boolean success;
}
