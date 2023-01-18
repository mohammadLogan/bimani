package com.atlas.ir.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Insurancecase.
 */
@Entity
@Table(name = "insurancecase")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Insurancecase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "casenumber")
    private Long casenumber;

    @Column(name = "occurdate")
    private Instant occurdate;

    @Column(name = "issuetracking")
    private String issuetracking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Insurancecase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCasenumber() {
        return this.casenumber;
    }

    public Insurancecase casenumber(Long casenumber) {
        this.setCasenumber(casenumber);
        return this;
    }

    public void setCasenumber(Long casenumber) {
        this.casenumber = casenumber;
    }

    public Instant getOccurdate() {
        return this.occurdate;
    }

    public Insurancecase occurdate(Instant occurdate) {
        this.setOccurdate(occurdate);
        return this;
    }

    public void setOccurdate(Instant occurdate) {
        this.occurdate = occurdate;
    }

    public String getIssuetracking() {
        return this.issuetracking;
    }

    public Insurancecase issuetracking(String issuetracking) {
        this.setIssuetracking(issuetracking);
        return this;
    }

    public void setIssuetracking(String issuetracking) {
        this.issuetracking = issuetracking;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Insurancecase)) {
            return false;
        }
        return id != null && id.equals(((Insurancecase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Insurancecase{" +
            "id=" + getId() +
            ", casenumber=" + getCasenumber() +
            ", occurdate='" + getOccurdate() + "'" +
            ", issuetracking='" + getIssuetracking() + "'" +
            "}";
    }
}
