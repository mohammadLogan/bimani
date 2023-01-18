package com.atlas.ir.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Caseaccidentu20.
 */
@Entity
@Table(name = "caseaccidentu_20")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Caseaccidentu20 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caseaccidentu20 id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Caseaccidentu20 date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caseaccidentu20)) {
            return false;
        }
        return id != null && id.equals(((Caseaccidentu20) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caseaccidentu20{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
