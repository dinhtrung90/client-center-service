package com.vts.clientcenter.kafka.messages;

import com.okta.sdk.resource.user.UserStatus;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedMessage {
    private String userId;
    private String email;
    private String message;
    private UserStatus status;
    private MessageType type;
}
