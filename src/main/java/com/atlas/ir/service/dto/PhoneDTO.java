package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Phone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhoneDTO implements Serializable {

    private Long id;

    private String number;

    private String phonetype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneDTO)) {
            return false;
        }

        PhoneDTO phoneDTO = (PhoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", phonetype='" + getPhonetype() + "'" +
            "}";
    }
}
