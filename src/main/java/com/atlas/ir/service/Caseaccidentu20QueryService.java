package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Caseaccidentu20;
import com.atlas.ir.repository.Caseaccidentu20Repository;
import com.atlas.ir.service.criteria.Caseaccidentu20Criteria;
import com.atlas.ir.service.dto.Caseaccidentu20DTO;
import com.atlas.ir.service.mapper.Caseaccidentu20Mapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Caseaccidentu20} entities in the database.
 * The main input is a {@link Caseaccidentu20Criteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Caseaccidentu20DTO} or a {@link Page} of {@link Caseaccidentu20DTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Caseaccidentu20QueryService extends QueryService<Caseaccidentu20> {

    private final Logger log = LoggerFactory.getLogger(Caseaccidentu20QueryService.class);

    private final Caseaccidentu20Repository caseaccidentu20Repository;

    private final Caseaccidentu20Mapper caseaccidentu20Mapper;

    public Caseaccidentu20QueryService(Caseaccidentu20Repository caseaccidentu20Repository, Caseaccidentu20Mapper caseaccidentu20Mapper) {
        this.caseaccidentu20Repository = caseaccidentu20Repository;
        this.caseaccidentu20Mapper = caseaccidentu20Mapper;
    }

    /**
     * Return a {@link List} of {@link Caseaccidentu20DTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Caseaccidentu20DTO> findByCriteria(Caseaccidentu20Criteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Caseaccidentu20> specification = createSpecification(criteria);
        return caseaccidentu20Mapper.toDto(caseaccidentu20Repository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link Caseaccidentu20DTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Caseaccidentu20DTO> findByCriteria(Caseaccidentu20Criteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Caseaccidentu20> specification = createSpecification(criteria);
        return caseaccidentu20Repository.findAll(specification, page).map(caseaccidentu20Mapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Caseaccidentu20Criteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Caseaccidentu20> specification = createSpecification(criteria);
        return caseaccidentu20Repository.count(specification);
    }

    /**
     * Function to convert {@link Caseaccidentu20Criteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Caseaccidentu20> createSpecification(Caseaccidentu20Criteria criteria) {
        Specification<Caseaccidentu20> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Caseaccidentu20_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Caseaccidentu20_.date));
            }
        }
        return specification;
    }
}
