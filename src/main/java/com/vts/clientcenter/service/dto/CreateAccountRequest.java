package com.vts.clientcenter.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.enumeration.Gender;
import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    @Size(min = 2, max = 10)
    private String langKey;

    @NotNull
    private String tempPassword;

    private Gender gender;

    private String mobilePhone;

    private String homePhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT_YYYY_MM_DD)
    private Date birthDate;

    private boolean isIsTempPassword;

}
