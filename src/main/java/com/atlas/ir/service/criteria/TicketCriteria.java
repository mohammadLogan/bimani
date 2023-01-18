package com.atlas.ir.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.atlas.ir.domain.Ticket} entity. This class is used
 * in {@link com.atlas.ir.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private InstantFilter issuedate;

    private InstantFilter lastmodifieddate;

    private Boolean distinct;

    public TicketCriteria() {}

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.issuedate = other.issuedate == null ? null : other.issuedate.copy();
        this.lastmodifieddate = other.lastmodifieddate == null ? null : other.lastmodifieddate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getIssuedate() {
        return issuedate;
    }

    public InstantFilter issuedate() {
        if (issuedate == null) {
            issuedate = new InstantFilter();
        }
        return issuedate;
    }

    public void setIssuedate(InstantFilter issuedate) {
        this.issuedate = issuedate;
    }

    public InstantFilter getLastmodifieddate() {
        return lastmodifieddate;
    }

    public InstantFilter lastmodifieddate() {
        if (lastmodifieddate == null) {
            lastmodifieddate = new InstantFilter();
        }
        return lastmodifieddate;
    }

    public void setLastmodifieddate(InstantFilter lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(issuedate, that.issuedate) &&
            Objects.equals(lastmodifieddate, that.lastmodifieddate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, issuedate, lastmodifieddate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (issuedate != null ? "issuedate=" + issuedate + ", " : "") +
            (lastmodifieddate != null ? "lastmodifieddate=" + lastmodifieddate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
