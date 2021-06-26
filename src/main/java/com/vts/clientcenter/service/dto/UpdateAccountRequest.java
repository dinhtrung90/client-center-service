package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.enumeration.AccountStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {

    @NotNull
    private String userId;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 2, max = 10)
    private String langKey;

    private Set<AuthorityDto> authorities;

    private AccountStatus accountStatus;

    private String tempPassword;

    private boolean isIsTempPassword;

    private List<UserAddressDTO> userAddressList;

    private UserProfileDTO userProfileDto;
}
