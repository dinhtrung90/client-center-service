package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.enumeration.OperationEnum;
import java.io.Serializable;

/**
 * A DTO for the {@link com.vts.clientcenter.domain.ModuleOperation} entity.
 */
public class ModuleOperationDTO implements Serializable {
    private Long id;

    private OperationEnum name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationEnum getName() {
        return name;
    }

    public void setName(OperationEnum name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleOperationDTO)) {
            return false;
        }

        return id != null && id.equals(((ModuleOperationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleOperationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
