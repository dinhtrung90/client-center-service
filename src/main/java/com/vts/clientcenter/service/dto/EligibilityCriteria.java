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
 * Criteria class for the {@link com.vts.clientcenter.domain.Eligibility} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.EligibilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eligibilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EligibilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter refId;

    private StringFilter fileUrl;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public EligibilityCriteria() {
    }

    public EligibilityCriteria(EligibilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.refId = other.refId == null ? null : other.refId.copy();
        this.fileUrl = other.fileUrl == null ? null : other.fileUrl.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public EligibilityCriteria copy() {
        return new EligibilityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getRefId() {
        return refId;
    }

    public void setRefId(StringFilter refId) {
        this.refId = refId;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EligibilityCriteria that = (EligibilityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(refId, that.refId) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileName,
        refId,
        fileUrl,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fileName != null ? "fileName=" + fileName + ", " : "") +
                (refId != null ? "refId=" + refId + ", " : "") +
                (fileUrl != null ? "fileUrl=" + fileUrl + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            "}";
    }

}
