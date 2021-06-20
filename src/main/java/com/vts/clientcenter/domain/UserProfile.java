package com.vts.clientcenter.domain;

import com.vts.clientcenter.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "tv_user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 100)
    @Column(name = "id", length = 100, nullable = false, unique = true)
    @Id
    private String id;

    @Column(name = "phone")
    @NotNull
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public UserProfile user(User user) {
        this.user = user;
        return this;
    }
}
