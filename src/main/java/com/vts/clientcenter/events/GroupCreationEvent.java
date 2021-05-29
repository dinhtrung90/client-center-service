package com.vts.clientcenter.events;

import com.okta.sdk.resource.group.Group;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreationEvent {
    private Group group;
}
