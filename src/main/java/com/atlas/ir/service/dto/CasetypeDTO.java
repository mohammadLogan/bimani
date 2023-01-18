package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Casetype} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CasetypeDTO implements Serializable {

    private Long id;

    private String name;

    private String parenttype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParenttype() {
        return parenttype;
    }

    public void setParenttype(String parenttype) {
        this.parenttype = parenttype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CasetypeDTO)) {
            return false;
        }

        CasetypeDTO casetypeDTO = (CasetypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, casetypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasetypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", parenttype='" + getParenttype() + "'" +
            "}";
    }
}
