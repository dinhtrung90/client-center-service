package com.vts.clientcenter.domain;

import com.vts.clientcenter.domain.enumeration.Gender;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Eligibility.
 */
@Entity
@Table(name = "tv_eligibility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Eligibility extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Size(max = 50)
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @NotNull
    @Size(max = 30)
    @Column(name = "phone", length = 30, nullable = false, unique = true)
    private String phone;

    @Size(max = 50)
    @Column(name = "full_name", length = 50)
    private String fullName;

    @NotNull
    @Size(max = 20)
    @Column(name = "ssn", length = 20, nullable = false, unique = true)
    private String ssn;

    @Column(name = "birth_day")
    private Instant birthDay;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "full_address")
    private String fullAddress;

    @OneToMany(mappedBy = "eligibility", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Set<EligibilityMetadata> eligibilityMetadata = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Eligibility email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Eligibility phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public Eligibility fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSsn() {
        return ssn;
    }

    public Eligibility ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public Eligibility birthDay(Instant birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void addEligibilityMetadata(EligibilityMetadata eligibility) {
        this.eligibilityMetadata.add(eligibility);
        eligibility.setEligibility(this);
    }

    public Eligibility removeEligibilityMetadata(EligibilityMetadata eligibility) {
        this.eligibilityMetadata.remove(eligibility);
        eligibility.setEligibility(null);
        return this;
    }

    public Set<EligibilityMetadata> getEligibilityMetadata() {
        return eligibilityMetadata;
    }

    public void setEligibilityMetadata(Set<EligibilityMetadata> eligibilityMetadata) {
        this.eligibilityMetadata = eligibilityMetadata;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eligibility)) {
            return false;
        }
        return id != null && id.equals(((Eligibility) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eligibility{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", ssn='" + getSsn() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            "}";
    }
}
