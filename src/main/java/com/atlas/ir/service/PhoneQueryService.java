package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Phone;
import com.atlas.ir.repository.PhoneRepository;
import com.atlas.ir.service.criteria.PhoneCriteria;
import com.atlas.ir.service.dto.PhoneDTO;
import com.atlas.ir.service.mapper.PhoneMapper;
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
 * Service for executing complex queries for {@link Phone} entities in the database.
 * The main input is a {@link PhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PhoneDTO} or a {@link Page} of {@link PhoneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhoneQueryService extends QueryService<Phone> {

    private final Logger log = LoggerFactory.getLogger(PhoneQueryService.class);

    private final PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper;

    public PhoneQueryService(PhoneRepository phoneRepository, PhoneMapper phoneMapper) {
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
    }

    /**
     * Return a {@link List} of {@link PhoneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PhoneDTO> findByCriteria(PhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneMapper.toDto(phoneRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PhoneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PhoneDTO> findByCriteria(PhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.findAll(specification, page).map(phoneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.count(specification);
    }

    /**
     * Function to convert {@link PhoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Phone> createSpecification(PhoneCriteria criteria) {
        Specification<Phone> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Phone_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Phone_.number));
            }
            if (criteria.getPhonetype() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhonetype(), Phone_.phonetype));
            }
        }
        return specification;
    }
}
