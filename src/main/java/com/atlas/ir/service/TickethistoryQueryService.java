package com.atlas.ir.service;

import com.atlas.ir.domain.*; // for static metamodels
import com.atlas.ir.domain.Tickethistory;
import com.atlas.ir.repository.TickethistoryRepository;
import com.atlas.ir.service.criteria.TickethistoryCriteria;
import com.atlas.ir.service.dto.TickethistoryDTO;
import com.atlas.ir.service.mapper.TickethistoryMapper;
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
 * Service for executing complex queries for {@link Tickethistory} entities in the database.
 * The main input is a {@link TickethistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TickethistoryDTO} or a {@link Page} of {@link TickethistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TickethistoryQueryService extends QueryService<Tickethistory> {

    private final Logger log = LoggerFactory.getLogger(TickethistoryQueryService.class);

    private final TickethistoryRepository tickethistoryRepository;

    private final TickethistoryMapper tickethistoryMapper;

    public TickethistoryQueryService(TickethistoryRepository tickethistoryRepository, TickethistoryMapper tickethistoryMapper) {
        this.tickethistoryRepository = tickethistoryRepository;
        this.tickethistoryMapper = tickethistoryMapper;
    }

    /**
     * Return a {@link List} of {@link TickethistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TickethistoryDTO> findByCriteria(TickethistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tickethistory> specification = createSpecification(criteria);
        return tickethistoryMapper.toDto(tickethistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TickethistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TickethistoryDTO> findByCriteria(TickethistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tickethistory> specification = createSpecification(criteria);
        return tickethistoryRepository.findAll(specification, page).map(tickethistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TickethistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tickethistory> specification = createSpecification(criteria);
        return tickethistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TickethistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tickethistory> createSpecification(TickethistoryCriteria criteria) {
        Specification<Tickethistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tickethistory_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tickethistory_.description));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Tickethistory_.date));
            }
        }
        return specification;
    }
}
