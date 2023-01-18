package com.atlas.ir.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Casetype.
 */
@Entity
@Table(name = "casetype")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Casetype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "parenttype")
    private String parenttype;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Casetype id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Casetype name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParenttype() {
        return this.parenttype;
    }

    public Casetype parenttype(String parenttype) {
        this.setParenttype(parenttype);
        return this;
    }

    public void setParenttype(String parenttype) {
        this.parenttype = parenttype;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Casetype)) {
            return false;
        }
        return id != null && id.equals(((Casetype) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Casetype{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", parenttype='" + getParenttype() + "'" +
            "}";
    }
}
