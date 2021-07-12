package com.vts.clientcenter.events;

import com.vts.clientcenter.domain.Organization;
import com.vts.clientcenter.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganizationCreatedEvent {
    private final Organization organization;

    public OrganizationCreatedEvent(Organization organization) {
        this.organization = organization;
    }
}
