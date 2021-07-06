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

    private String name;

    private String desc;

    private boolean isSelected;
}
