package com.atlas.ir.service.criteria;

import com.atlas.ir.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.atlas.ir.domain.Operator} entity. This class is used
 * in {@link com.atlas.ir.web.rest.OperatorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /operators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperatorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private LongFilter nationalid;

    private InstantFilter dateofbirth;

    private StatusFilter status;

    private StringFilter gender;

    private Boolean distinct;

    public OperatorCriteria() {}

    public OperatorCriteria(OperatorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.nationalid = other.nationalid == null ? null : other.nationalid.copy();
        this.dateofbirth = other.dateofbirth == null ? null : other.dateofbirth.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OperatorCriteria copy() {
        return new OperatorCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSurname() {
        return surname;
    }

    public StringFilter surname() {
        if (surname == null) {
            surname = new StringFilter();
        }
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public LongFilter getNationalid() {
        return nationalid;
    }

    public LongFilter nationalid() {
        if (nationalid == null) {
            nationalid = new LongFilter();
        }
        return nationalid;
    }

    public void setNationalid(LongFilter nationalid) {
        this.nationalid = nationalid;
    }

    public InstantFilter getDateofbirth() {
        return dateofbirth;
    }

    public InstantFilter dateofbirth() {
        if (dateofbirth == null) {
            dateofbirth = new InstantFilter();
        }
        return dateofbirth;
    }

    public void setDateofbirth(InstantFilter dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public StringFilter getGender() {
        return gender;
    }

    public StringFilter gender() {
        if (gender == null) {
            gender = new StringFilter();
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
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
        final OperatorCriteria that = (OperatorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(nationalid, that.nationalid) &&
            Objects.equals(dateofbirth, that.dateofbirth) &&
            Objects.equals(status, that.status) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, nationalid, dateofbirth, status, gender, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (surname != null ? "surname=" + surname + ", " : "") +
            (nationalid != null ? "nationalid=" + nationalid + ", " : "") +
            (dateofbirth != null ? "dateofbirth=" + dateofbirth + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
