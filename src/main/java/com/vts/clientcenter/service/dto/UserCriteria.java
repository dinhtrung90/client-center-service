package com.vts.clientcenter.service.dto;

import com.okta.sdk.resource.user.UserStatus;
import com.vts.clientcenter.domain.*;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.StringFilter;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@NoArgsConstructor
public class UserCriteria implements Serializable, Criteria {

    public static class UserStatusFilter extends Filter<UserStatus> {

        public UserStatusFilter() {
        }

        public UserStatusFilter(UserCriteria.UserStatusFilter filter) {
            super(filter);
        }

        @Override
        public UserCriteria.UserStatusFilter copy() {
            return new UserCriteria.UserStatusFilter(this);
        }

    }

    private StringFilter id;

    private StringFilter email;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private StringFilter createdBy;

    private StringFilter lastModifiedBy;

    private UserStatusFilter  status;

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }

}
