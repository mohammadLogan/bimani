package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Repairshop;
import com.atlas.ir.repository.RepairshopRepository;
import com.atlas.ir.service.criteria.RepairshopCriteria;
import com.atlas.ir.service.dto.RepairshopDTO;
import com.atlas.ir.service.mapper.RepairshopMapper;
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
 * Service for executing complex queries for {@link Repairshop} entities in the database.
 * The main input is a {@link RepairshopCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RepairshopDTO} or a {@link Page} of {@link RepairshopDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairshopQueryService extends QueryService<Repairshop> {

    private final Logger log = LoggerFactory.getLogger(RepairshopQueryService.class);

    private final RepairshopRepository repairshopRepository;

    private final RepairshopMapper repairshopMapper;

    public RepairshopQueryService(RepairshopRepository repairshopRepository, RepairshopMapper repairshopMapper) {
        this.repairshopRepository = repairshopRepository;
        this.repairshopMapper = repairshopMapper;
    }

    /**
     * Return a {@link List} of {@link RepairshopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RepairshopDTO> findByCriteria(RepairshopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Repairshop> specification = createSpecification(criteria);
        return repairshopMapper.toDto(repairshopRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RepairshopDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairshopDTO> findByCriteria(RepairshopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Repairshop> specification = createSpecification(criteria);
        return repairshopRepository.findAll(specification, page).map(repairshopMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RepairshopCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Repairshop> specification = createSpecification(criteria);
        return repairshopRepository.count(specification);
    }

    /**
     * Function to convert {@link RepairshopCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Repairshop> createSpecification(RepairshopCriteria criteria) {
        Specification<Repairshop> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Repairshop_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Repairshop_.name));
            }
        }
        return specification;
    }
}
