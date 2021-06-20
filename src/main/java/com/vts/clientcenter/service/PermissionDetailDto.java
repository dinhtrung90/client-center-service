package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.enumeration.OperationEnum;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDetailDto {
    private Long id;

    @NotNull
    private String permissionName;

    @NotNull
    private Long permissionId;

    private String permissionDesc;

    private List<OperationEnum> operations;
}
