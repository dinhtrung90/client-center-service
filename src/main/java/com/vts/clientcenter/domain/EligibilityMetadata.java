package com.vts.clientcenter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * A EligibilityMetadata.
 */
@Entity
@Table(name = "tv_eligibility_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EligibilityMetadata extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "signature")
    private String signature;

    @Column(name = "thumb_url")
    private String thumbUrl;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eligibility_id")
    private Eligibility eligibility;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public EligibilityMetadata thumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public Eligibility getEligibility() {
        return eligibility;
    }

    public void setEligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EligibilityMetadata)) {
            return false;
        }
        return id != null && id.equals(((EligibilityMetadata) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EligibilityMetadata{" +
            "id=" + getId() +
            ", thumbUrl='" + getThumbUrl() + "'" +
            "}";
    }
}
