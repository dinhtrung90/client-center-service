package com.vts.clientcenter.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.EligibilityMetadata} entity.
 */
public class EligibilityMetadataDTO implements Serializable {

    private String id;

    private String thumbUrl;

    private String fileName;

    private String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibilityMetadataDTO)) {
            return false;
        }

        return id != null && id.equals(((EligibilityMetadataDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityMetadataDTO{" +
            "id=" + getId() +
            ", thumbUrl='" + getThumbUrl() + "'" +
            "}";
    }
}
