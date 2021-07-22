package com.vts.clientcenter.service.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryEligibilityRequest {
    private String userId;
    private String description;
}
