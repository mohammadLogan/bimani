package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Ticketstatus;
import com.atlas.ir.repository.TicketstatusRepository;
import com.atlas.ir.service.criteria.TicketstatusCriteria;
import com.atlas.ir.service.dto.TicketstatusDTO;
import com.atlas.ir.service.mapper.TicketstatusMapper;
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
 * Service for executing complex queries for {@link Ticketstatus} entities in the database.
 * The main input is a {@link TicketstatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketstatusDTO} or a {@link Page} of {@link TicketstatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketstatusQueryService extends QueryService<Ticketstatus> {

    private final Logger log = LoggerFactory.getLogger(TicketstatusQueryService.class);

    private final TicketstatusRepository ticketstatusRepository;

    private final TicketstatusMapper ticketstatusMapper;

    public TicketstatusQueryService(TicketstatusRepository ticketstatusRepository, TicketstatusMapper ticketstatusMapper) {
        this.ticketstatusRepository = ticketstatusRepository;
        this.ticketstatusMapper = ticketstatusMapper;
    }

    /**
     * Return a {@link List} of {@link TicketstatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketstatusDTO> findByCriteria(TicketstatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticketstatus> specification = createSpecification(criteria);
        return ticketstatusMapper.toDto(ticketstatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketstatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketstatusDTO> findByCriteria(TicketstatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticketstatus> specification = createSpecification(criteria);
        return ticketstatusRepository.findAll(specification, page).map(ticketstatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketstatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ticketstatus> specification = createSpecification(criteria);
        return ticketstatusRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketstatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticketstatus> createSpecification(TicketstatusCriteria criteria) {
        Specification<Ticketstatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticketstatus_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ticketstatus_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ticketstatus_.description));
            }
        }
        return specification;
    }
}
