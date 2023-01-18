package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Operator;
import com.atlas.ir.repository.OperatorRepository;
import com.atlas.ir.service.criteria.OperatorCriteria;
import com.atlas.ir.service.dto.OperatorDTO;
import com.atlas.ir.service.mapper.OperatorMapper;
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
 * Service for executing complex queries for {@link Operator} entities in the database.
 * The main input is a {@link OperatorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OperatorDTO} or a {@link Page} of {@link OperatorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperatorQueryService extends QueryService<Operator> {

    private final Logger log = LoggerFactory.getLogger(OperatorQueryService.class);

    private final OperatorRepository operatorRepository;

    private final OperatorMapper operatorMapper;

    public OperatorQueryService(OperatorRepository operatorRepository, OperatorMapper operatorMapper) {
        this.operatorRepository = operatorRepository;
        this.operatorMapper = operatorMapper;
    }

    /**
     * Return a {@link List} of {@link OperatorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OperatorDTO> findByCriteria(OperatorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Operator> specification = createSpecification(criteria);
        return operatorMapper.toDto(operatorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OperatorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperatorDTO> findByCriteria(OperatorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Operator> specification = createSpecification(criteria);
        return operatorRepository.findAll(specification, page).map(operatorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OperatorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Operator> specification = createSpecification(criteria);
        return operatorRepository.count(specification);
    }

    /**
     * Function to convert {@link OperatorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Operator> createSpecification(OperatorCriteria criteria) {
        Specification<Operator> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Operator_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Operator_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Operator_.surname));
            }
            if (criteria.getNationalid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNationalid(), Operator_.nationalid));
            }
            if (criteria.getDateofbirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateofbirth(), Operator_.dateofbirth));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Operator_.status));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGender(), Operator_.gender));
            }
        }
        return specification;
    }
}
