package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Casetype;
import com.atlas.ir.repository.CasetypeRepository;
import com.atlas.ir.service.criteria.CasetypeCriteria;
import com.atlas.ir.service.dto.CasetypeDTO;
import com.atlas.ir.service.mapper.CasetypeMapper;
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
 * Service for executing complex queries for {@link Casetype} entities in the database.
 * The main input is a {@link CasetypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CasetypeDTO} or a {@link Page} of {@link CasetypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CasetypeQueryService extends QueryService<Casetype> {

    private final Logger log = LoggerFactory.getLogger(CasetypeQueryService.class);

    private final CasetypeRepository casetypeRepository;

    private final CasetypeMapper casetypeMapper;

    public CasetypeQueryService(CasetypeRepository casetypeRepository, CasetypeMapper casetypeMapper) {
        this.casetypeRepository = casetypeRepository;
        this.casetypeMapper = casetypeMapper;
    }

    /**
     * Return a {@link List} of {@link CasetypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CasetypeDTO> findByCriteria(CasetypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Casetype> specification = createSpecification(criteria);
        return casetypeMapper.toDto(casetypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CasetypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CasetypeDTO> findByCriteria(CasetypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Casetype> specification = createSpecification(criteria);
        return casetypeRepository.findAll(specification, page).map(casetypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CasetypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Casetype> specification = createSpecification(criteria);
        return casetypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CasetypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Casetype> createSpecification(CasetypeCriteria criteria) {
        Specification<Casetype> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Casetype_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Casetype_.name));
            }
            if (criteria.getParenttype() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParenttype(), Casetype_.parenttype));
            }
        }
        return specification;
    }
}
