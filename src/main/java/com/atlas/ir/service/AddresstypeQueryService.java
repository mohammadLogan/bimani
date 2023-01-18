package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Addresstype;
import com.atlas.ir.repository.AddresstypeRepository;
import com.atlas.ir.service.criteria.AddresstypeCriteria;
import com.atlas.ir.service.dto.AddresstypeDTO;
import com.atlas.ir.service.mapper.AddresstypeMapper;
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
 * Service for executing complex queries for {@link Addresstype} entities in the database.
 * The main input is a {@link AddresstypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddresstypeDTO} or a {@link Page} of {@link AddresstypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddresstypeQueryService extends QueryService<Addresstype> {

    private final Logger log = LoggerFactory.getLogger(AddresstypeQueryService.class);

    private final AddresstypeRepository addresstypeRepository;

    private final AddresstypeMapper addresstypeMapper;

    public AddresstypeQueryService(AddresstypeRepository addresstypeRepository, AddresstypeMapper addresstypeMapper) {
        this.addresstypeRepository = addresstypeRepository;
        this.addresstypeMapper = addresstypeMapper;
    }

    /**
     * Return a {@link List} of {@link AddresstypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddresstypeDTO> findByCriteria(AddresstypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Addresstype> specification = createSpecification(criteria);
        return addresstypeMapper.toDto(addresstypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddresstypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddresstypeDTO> findByCriteria(AddresstypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Addresstype> specification = createSpecification(criteria);
        return addresstypeRepository.findAll(specification, page).map(addresstypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddresstypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Addresstype> specification = createSpecification(criteria);
        return addresstypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AddresstypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Addresstype> createSpecification(AddresstypeCriteria criteria) {
        Specification<Addresstype> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Addresstype_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Addresstype_.name));
            }
        }
        return specification;
    }
}
