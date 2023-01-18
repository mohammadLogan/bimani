package com.atlas.ir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.atlas.ir.domain.Insurancecase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancecaseDTO implements Serializable {

    private Long id;

    private Long casenumber;

    private Instant occurdate;

    private String issuetracking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCasenumber() {
        return casenumber;
    }

    public void setCasenumber(Long casenumber) {
        this.casenumber = casenumber;
    }

    public Instant getOccurdate() {
        return occurdate;
    }

    public void setOccurdate(Instant occurdate) {
        this.occurdate = occurdate;
    }

    public String getIssuetracking() {
        return issuetracking;
    }

    public void setIssuetracking(String issuetracking) {
        this.issuetracking = issuetracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsurancecaseDTO)) {
            return false;
        }

        InsurancecaseDTO insurancecaseDTO = (InsurancecaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, insurancecaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancecaseDTO{" +
            "id=" + getId() +
            ", casenumber=" + getCasenumber() +
            ", occurdate='" + getOccurdate() + "'" +
            ", issuetracking='" + getIssuetracking() + "'" +
            "}";
    }
}
