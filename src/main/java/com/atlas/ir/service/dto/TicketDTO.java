package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Ticket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketDTO implements Serializable {

    private Long id;

    private String description;

    private Instant issuedate;

    private Instant lastmodifieddate;

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

    public Instant getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(Instant issuedate) {
        this.issuedate = issuedate;
    }

    public Instant getLastmodifieddate() {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(Instant lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        TicketDTO ticketDTO = (TicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", issuedate='" + getIssuedate() + "'" +
            ", lastmodifieddate='" + getLastmodifieddate() + "'" +
            "}";
    }
}
