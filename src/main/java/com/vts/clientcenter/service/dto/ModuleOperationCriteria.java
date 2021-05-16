package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.enumeration.OperationEnum;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.vts.clientcenter.domain.ModuleOperation} entity. This class is used
 * in {@link com.vts.clientcenter.web.rest.ModuleOperationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /module-operations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ModuleOperationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OperationEnum
     */
    public static class OperationEnumFilter extends Filter<OperationEnum> {

        public OperationEnumFilter() {}

        public OperationEnumFilter(OperationEnumFilter filter) {
            super(filter);
        }

        @Override
        public OperationEnumFilter copy() {
            return new OperationEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OperationEnumFilter name;

    public ModuleOperationCriteria() {}

    public ModuleOperationCriteria(ModuleOperationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
    }

    @Override
    public ModuleOperationCriteria copy() {
        return new ModuleOperationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public OperationEnumFilter getName() {
        return name;
    }

    public void setName(OperationEnumFilter name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ModuleOperationCriteria that = (ModuleOperationCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleOperationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
            "}";
    }
}
