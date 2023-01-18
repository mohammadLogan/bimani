package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Tickethistory;
import com.atlas.ir.repository.TickethistoryRepository;
import com.atlas.ir.service.TickethistoryService;
import com.atlas.ir.service.dto.TickethistoryDTO;
import com.atlas.ir.service.mapper.TickethistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tickethistory}.
 */
@Service
@Transactional
public class TickethistoryServiceImpl implements TickethistoryService {

    private final Logger log = LoggerFactory.getLogger(TickethistoryServiceImpl.class);

    private final TickethistoryRepository tickethistoryRepository;

    private final TickethistoryMapper tickethistoryMapper;

    public TickethistoryServiceImpl(TickethistoryRepository tickethistoryRepository, TickethistoryMapper tickethistoryMapper) {
        this.tickethistoryRepository = tickethistoryRepository;
        this.tickethistoryMapper = tickethistoryMapper;
    }

    @Override
    public TickethistoryDTO save(TickethistoryDTO tickethistoryDTO) {
        log.debug("Request to save Tickethistory : {}", tickethistoryDTO);
        Tickethistory tickethistory = tickethistoryMapper.toEntity(tickethistoryDTO);
        tickethistory = tickethistoryRepository.save(tickethistory);
        return tickethistoryMapper.toDto(tickethistory);
    }

    @Override
    public TickethistoryDTO update(TickethistoryDTO tickethistoryDTO) {
        log.debug("Request to update Tickethistory : {}", tickethistoryDTO);
        Tickethistory tickethistory = tickethistoryMapper.toEntity(tickethistoryDTO);
        tickethistory = tickethistoryRepository.save(tickethistory);
        return tickethistoryMapper.toDto(tickethistory);
    }

    @Override
    public Optional<TickethistoryDTO> partialUpdate(TickethistoryDTO tickethistoryDTO) {
        log.debug("Request to partially update Tickethistory : {}", tickethistoryDTO);

        return tickethistoryRepository
            .findById(tickethistoryDTO.getId())
            .map(existingTickethistory -> {
                tickethistoryMapper.partialUpdate(existingTickethistory, tickethistoryDTO);

                return existingTickethistory;
            })
            .map(tickethistoryRepository::save)
            .map(tickethistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TickethistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tickethistories");
        return tickethistoryRepository.findAll(pageable).map(tickethistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TickethistoryDTO> findOne(Long id) {
        log.debug("Request to get Tickethistory : {}", id);
        return tickethistoryRepository.findById(id).map(tickethistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tickethistory : {}", id);
        tickethistoryRepository.deleteById(id);
    }
}
