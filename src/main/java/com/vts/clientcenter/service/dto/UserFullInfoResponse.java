package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFullInfoResponse {

    private UserDTO userDto;

    private UserProfileDTO userProfileDto;

}
