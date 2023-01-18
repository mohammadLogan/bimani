package com.atlas.ir.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.atlas.ir.domain.Insurancecase} entity. This class is used
 * in {@link com.atlas.ir.web.rest.InsurancecaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurancecases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancecaseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter casenumber;

    private InstantFilter occurdate;

    private StringFilter issuetracking;

    private Boolean distinct;

    public InsurancecaseCriteria() {}

    public InsurancecaseCriteria(InsurancecaseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.casenumber = other.casenumber == null ? null : other.casenumber.copy();
        this.occurdate = other.occurdate == null ? null : other.occurdate.copy();
        this.issuetracking = other.issuetracking == null ? null : other.issuetracking.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InsurancecaseCriteria copy() {
        return new InsurancecaseCriteria(this);
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

    public LongFilter getCasenumber() {
        return casenumber;
    }

    public LongFilter casenumber() {
        if (casenumber == null) {
            casenumber = new LongFilter();
        }
        return casenumber;
    }

    public void setCasenumber(LongFilter casenumber) {
        this.casenumber = casenumber;
    }

    public InstantFilter getOccurdate() {
        return occurdate;
    }

    public InstantFilter occurdate() {
        if (occurdate == null) {
            occurdate = new InstantFilter();
        }
        return occurdate;
    }

    public void setOccurdate(InstantFilter occurdate) {
        this.occurdate = occurdate;
    }

    public StringFilter getIssuetracking() {
        return issuetracking;
    }

    public StringFilter issuetracking() {
        if (issuetracking == null) {
            issuetracking = new StringFilter();
        }
        return issuetracking;
    }

    public void setIssuetracking(StringFilter issuetracking) {
        this.issuetracking = issuetracking;
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
        final InsurancecaseCriteria that = (InsurancecaseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(casenumber, that.casenumber) &&
            Objects.equals(occurdate, that.occurdate) &&
            Objects.equals(issuetracking, that.issuetracking) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, casenumber, occurdate, issuetracking, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancecaseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (casenumber != null ? "casenumber=" + casenumber + ", " : "") +
            (occurdate != null ? "occurdate=" + occurdate + ", " : "") +
            (issuetracking != null ? "issuetracking=" + issuetracking + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
