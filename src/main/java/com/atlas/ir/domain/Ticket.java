package com.atlas.ir.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "issuedate")
    private Instant issuedate;

    @Column(name = "lastmodifieddate")
    private Instant lastmodifieddate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Ticket description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getIssuedate() {
        return this.issuedate;
    }

    public Ticket issuedate(Instant issuedate) {
        this.setIssuedate(issuedate);
        return this;
    }

    public void setIssuedate(Instant issuedate) {
        this.issuedate = issuedate;
    }

    public Instant getLastmodifieddate() {
        return this.lastmodifieddate;
    }

    public Ticket lastmodifieddate(Instant lastmodifieddate) {
        this.setLastmodifieddate(lastmodifieddate);
        return this;
    }

    public void setLastmodifieddate(Instant lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", issuedate='" + getIssuedate() + "'" +
            ", lastmodifieddate='" + getLastmodifieddate() + "'" +
            "}";
    }
}
