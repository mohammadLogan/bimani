package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Cau20status;
import com.atlas.ir.repository.Cau20statusRepository;
import com.atlas.ir.service.criteria.Cau20statusCriteria;
import com.atlas.ir.service.dto.Cau20statusDTO;
import com.atlas.ir.service.mapper.Cau20statusMapper;
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
 * Service for executing complex queries for {@link Cau20status} entities in the database.
 * The main input is a {@link Cau20statusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cau20statusDTO} or a {@link Page} of {@link Cau20statusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Cau20statusQueryService extends QueryService<Cau20status> {

    private final Logger log = LoggerFactory.getLogger(Cau20statusQueryService.class);

    private final Cau20statusRepository cau20statusRepository;

    private final Cau20statusMapper cau20statusMapper;

    public Cau20statusQueryService(Cau20statusRepository cau20statusRepository, Cau20statusMapper cau20statusMapper) {
        this.cau20statusRepository = cau20statusRepository;
        this.cau20statusMapper = cau20statusMapper;
    }

    /**
     * Return a {@link List} of {@link Cau20statusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cau20statusDTO> findByCriteria(Cau20statusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cau20status> specification = createSpecification(criteria);
        return cau20statusMapper.toDto(cau20statusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link Cau20statusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cau20statusDTO> findByCriteria(Cau20statusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cau20status> specification = createSpecification(criteria);
        return cau20statusRepository.findAll(specification, page).map(cau20statusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Cau20statusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cau20status> specification = createSpecification(criteria);
        return cau20statusRepository.count(specification);
    }

    /**
     * Function to convert {@link Cau20statusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cau20status> createSpecification(Cau20statusCriteria criteria) {
        Specification<Cau20status> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cau20status_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cau20status_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Cau20status_.description));
            }
        }
        return specification;
    }
}
