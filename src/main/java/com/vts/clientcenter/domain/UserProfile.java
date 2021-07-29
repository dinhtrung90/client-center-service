package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vts.clientcenter.domain.enumeration.Gender;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

@Document(indexName = "profile_index", type = "profile")
public class UserProfile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "phone")
    @NotNull
    @Field(type = FieldType.Keyword)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @Field(type = FieldType.Keyword)
    private Gender gender;

    @Column(name = "birth_date")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date birthDate;

    @Column(name = "home_phone")
    @Field(type = FieldType.Keyword)
    private String homePhone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private User user;

    public UserProfile addUser(User user) {
        if (this.user != null) return this;
        this.user = user;
        user.setUserProfile(this);
        return this;
    }
}
