package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Tickethistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TickethistoryDTO implements Serializable {

    private Long id;

    private String description;

    private Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof TickethistoryDTO)) {
            return false;
        }

        TickethistoryDTO tickethistoryDTO = (TickethistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tickethistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickethistoryDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
