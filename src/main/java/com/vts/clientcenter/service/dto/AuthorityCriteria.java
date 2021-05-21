package com.vts.clientcenter.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.StringFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
public class AuthorityCriteria  implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter roleName;

    public AuthorityCriteria() {}

    public AuthorityCriteria(AuthorityCriteria other){
        this.roleName = other.roleName == null ? null : other.roleName.copy();
    }

    @Override
    public Criteria copy() {
        return new AuthorityCriteria(this);
    }
}
