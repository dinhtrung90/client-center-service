package com.vts.clientcenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A EligibilityPresentStatus.
 */
@Entity
@Table(name = "tv_eligibility_present_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EligibilityPresentStatus extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "has_present")
    private Boolean hasPresent;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Eligibility eligibility;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public EligibilityPresentStatus code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public EligibilityPresentStatus qrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
        return this;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public Boolean isHasPresent() {
        return hasPresent;
    }

    public EligibilityPresentStatus hasPresent(Boolean hasPresent) {
        this.hasPresent = hasPresent;
        return this;
    }

    public void setHasPresent(Boolean hasPresent) {
        this.hasPresent = hasPresent;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public EligibilityPresentStatus expiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Eligibility getEligibility() {
        return eligibility;
    }

    public EligibilityPresentStatus eligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
        return this;
    }

    public void setEligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibilityPresentStatus)) {
            return false;
        }
        return id != null && id.equals(((EligibilityPresentStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityPresentStatus{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", qrCodeUrl='" + getQrCodeUrl() + "'" +
            ", hasPresent='" + isHasPresent() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            "}";
    }
}
