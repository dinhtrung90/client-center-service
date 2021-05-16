package com.vts.clientcenter.domain;

import com.vts.clientcenter.domain.enumeration.OperationEnum;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ModuleOperation.
 */
@Entity
@Table(name = "tv_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModuleOperation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private OperationEnum name;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationEnum getName() {
        return name;
    }

    public ModuleOperation name(OperationEnum name) {
        this.name = name;
        return this;
    }

    public void setName(OperationEnum name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleOperation)) {
            return false;
        }
        return id != null && id.equals(((ModuleOperation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleOperation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
