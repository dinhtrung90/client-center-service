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

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.EligibilityMetadata} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EligibilityMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eligibility-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EligibilityMetadataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter thumbUrl;

    public EligibilityMetadataCriteria() {
    }

    public EligibilityMetadataCriteria(EligibilityMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.thumbUrl = other.thumbUrl == null ? null : other.thumbUrl.copy();
    }

    @Override
    public EligibilityMetadataCriteria copy() {
        return new EligibilityMetadataCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(StringFilter thumbUrl) {
        this.thumbUrl = thumbUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EligibilityMetadataCriteria that = (EligibilityMetadataCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(thumbUrl, that.thumbUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        thumbUrl
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityMetadataCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (thumbUrl != null ? "thumbUrl=" + thumbUrl + ", " : "") +
            "}";
    }

}
