package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Caseaccidentu20} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Caseaccidentu20DTO implements Serializable {

    private Long id;

    private Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caseaccidentu20DTO)) {
            return false;
        }

        Caseaccidentu20DTO caseaccidentu20DTO = (Caseaccidentu20DTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caseaccidentu20DTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caseaccidentu20DTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
