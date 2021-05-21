package com.vts.clientcenter.service.dto;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {
    private String roleName;

    private Instant createDate;

    private Instant modifiedDate;

    private String description;
}
