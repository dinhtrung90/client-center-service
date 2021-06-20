package com.vts.clientcenter.service.dto;


import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.enumeration.Gender;
import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;
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

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<AuthorityDto> authorities;

    @NotNull
    private String tempPassword;

    private boolean isIsTempPassword;

    private boolean isIsEnable;

    private List<UserAddressDTO> userAddressList;

    private UserProfileDTO userProfileDto;
}
