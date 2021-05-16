package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.service.PermissionDetailDto;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditPermissionResponseDto {
    private String roleName;

    private String description;

    private Instant lastModifiedDate;

    private String modifiedBy;

    private List<PermissionDetailDto> permissionDetails;
}
