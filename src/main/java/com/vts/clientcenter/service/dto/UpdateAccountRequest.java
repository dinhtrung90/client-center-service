package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.domain.enumeration.Gender;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
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

    private AccountStatus accountStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT_YYYY_MM_DD)
    private Date birthDate;

    private Gender gender;

    private String mobilePhone;

    private String tempPassword;

    private boolean isIsTempPassword;

    private boolean isEnable;

    private boolean isVerifiedEmail;

    private boolean isApproved;


}
