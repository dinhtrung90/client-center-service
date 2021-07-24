package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
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

    private String id;

    @NotNull
    private String phone;

    private String homePhone;

    private String avatarUrl;

    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT_YYYY_MM_DD)
    private Date birthDate;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;
}
