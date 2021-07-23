package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.EligibilityMetadata;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityDetailDto {

    private EligibilityDTO eligibility;

    private List<EligibilityMetadataDTO> metadata;

    private List<EligibilityPresentStatusDTO> progress;
}
