package com.vts.clientcenter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.EligibilityPresentStatus} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EligibilityPresentStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eligibility-present-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EligibilityPresentStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter qrCodeUrl;

    private BooleanFilter hasPresent;

    private InstantFilter expiredAt;

    private LongFilter eligibilityId;

    public EligibilityPresentStatusCriteria() {
    }

    public EligibilityPresentStatusCriteria(EligibilityPresentStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.qrCodeUrl = other.qrCodeUrl == null ? null : other.qrCodeUrl.copy();
        this.hasPresent = other.hasPresent == null ? null : other.hasPresent.copy();
        this.expiredAt = other.expiredAt == null ? null : other.expiredAt.copy();
        this.eligibilityId = other.eligibilityId == null ? null : other.eligibilityId.copy();
    }

    @Override
    public EligibilityPresentStatusCriteria copy() {
        return new EligibilityPresentStatusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(StringFilter qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public BooleanFilter getHasPresent() {
        return hasPresent;
    }

    public void setHasPresent(BooleanFilter hasPresent) {
        this.hasPresent = hasPresent;
    }

    public InstantFilter getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(InstantFilter expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LongFilter getEligibilityId() {
        return eligibilityId;
    }

    public void setEligibilityId(LongFilter eligibilityId) {
        this.eligibilityId = eligibilityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EligibilityPresentStatusCriteria that = (EligibilityPresentStatusCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(qrCodeUrl, that.qrCodeUrl) &&
            Objects.equals(hasPresent, that.hasPresent) &&
            Objects.equals(expiredAt, that.expiredAt) &&
            Objects.equals(eligibilityId, that.eligibilityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        qrCodeUrl,
        hasPresent,
        expiredAt,
        eligibilityId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityPresentStatusCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (qrCodeUrl != null ? "qrCodeUrl=" + qrCodeUrl + ", " : "") +
                (hasPresent != null ? "hasPresent=" + hasPresent + ", " : "") +
                (expiredAt != null ? "expiredAt=" + expiredAt + ", " : "") +
                (eligibilityId != null ? "eligibilityId=" + eligibilityId + ", " : "") +
            "}";
    }

}
