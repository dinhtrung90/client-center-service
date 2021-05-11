package com.vts.clientcenter.events;

import com.vts.clientcenter.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent {
    private final User user;

    public UserCreatedEvent(User user) {
        this.user = user;
    }
}
