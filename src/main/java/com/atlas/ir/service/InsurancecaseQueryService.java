package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Insurancecase;
import com.atlas.ir.repository.InsurancecaseRepository;
import com.atlas.ir.service.criteria.InsurancecaseCriteria;
import com.atlas.ir.service.dto.InsurancecaseDTO;
import com.atlas.ir.service.mapper.InsurancecaseMapper;
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
 * Service for executing complex queries for {@link Insurancecase} entities in the database.
 * The main input is a {@link InsurancecaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InsurancecaseDTO} or a {@link Page} of {@link InsurancecaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsurancecaseQueryService extends QueryService<Insurancecase> {

    private final Logger log = LoggerFactory.getLogger(InsurancecaseQueryService.class);

    private final InsurancecaseRepository insurancecaseRepository;

    private final InsurancecaseMapper insurancecaseMapper;

    public InsurancecaseQueryService(InsurancecaseRepository insurancecaseRepository, InsurancecaseMapper insurancecaseMapper) {
        this.insurancecaseRepository = insurancecaseRepository;
        this.insurancecaseMapper = insurancecaseMapper;
    }

    /**
     * Return a {@link List} of {@link InsurancecaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InsurancecaseDTO> findByCriteria(InsurancecaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Insurancecase> specification = createSpecification(criteria);
        return insurancecaseMapper.toDto(insurancecaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InsurancecaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsurancecaseDTO> findByCriteria(InsurancecaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Insurancecase> specification = createSpecification(criteria);
        return insurancecaseRepository.findAll(specification, page).map(insurancecaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsurancecaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Insurancecase> specification = createSpecification(criteria);
        return insurancecaseRepository.count(specification);
    }

    /**
     * Function to convert {@link InsurancecaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Insurancecase> createSpecification(InsurancecaseCriteria criteria) {
        Specification<Insurancecase> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Insurancecase_.id));
            }
            if (criteria.getCasenumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCasenumber(), Insurancecase_.casenumber));
            }
            if (criteria.getOccurdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOccurdate(), Insurancecase_.occurdate));
            }
            if (criteria.getIssuetracking() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuetracking(), Insurancecase_.issuetracking));
            }
        }
        return specification;
    }
}
