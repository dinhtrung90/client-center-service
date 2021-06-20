package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import lombok.*;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.UserProfile} entity.
 */

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO implements Serializable {
    @NotNull
    @Size(max = 20)
    private String id;

    @NotNull
    private String phone;

    private String homePhone;

    private String avatarUrl;

    private Gender gender;

    private Instant birthDate;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;
}
