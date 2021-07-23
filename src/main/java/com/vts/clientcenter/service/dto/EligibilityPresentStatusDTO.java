package com.vts.clientcenter.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.EligibilityPresentStatus} entity.
 */
public class EligibilityPresentStatusDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String code;

    private String qrCodeUrl;

    private Boolean hasPresent;

    private Instant expiredAt;


    private Long eligibilityId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public Boolean isHasPresent() {
        return hasPresent;
    }

    public void setHasPresent(Boolean hasPresent) {
        this.hasPresent = hasPresent;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Long getEligibilityId() {
        return eligibilityId;
    }

    public void setEligibilityId(Long eligibilityId) {
        this.eligibilityId = eligibilityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibilityPresentStatusDTO)) {
            return false;
        }

        return id != null && id.equals(((EligibilityPresentStatusDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityPresentStatusDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", qrCodeUrl='" + getQrCodeUrl() + "'" +
            ", hasPresent='" + isHasPresent() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", eligibilityId=" + getEligibilityId() +
            "}";
    }
}
