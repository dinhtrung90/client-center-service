package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.service.PermissionDetailDto;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditPermissionRequestDto {
    private String roleName;

    private String description;

    private List<PermissionDetailDto> permissionDetails;
}
