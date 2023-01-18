package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Ticketstatus;
import com.atlas.ir.repository.TicketstatusRepository;
import com.atlas.ir.service.TicketstatusService;
import com.atlas.ir.service.dto.TicketstatusDTO;
import com.atlas.ir.service.mapper.TicketstatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ticketstatus}.
 */
@Service
@Transactional
public class TicketstatusServiceImpl implements TicketstatusService {

    private final Logger log = LoggerFactory.getLogger(TicketstatusServiceImpl.class);

    private final TicketstatusRepository ticketstatusRepository;

    private final TicketstatusMapper ticketstatusMapper;

    public TicketstatusServiceImpl(TicketstatusRepository ticketstatusRepository, TicketstatusMapper ticketstatusMapper) {
        this.ticketstatusRepository = ticketstatusRepository;
        this.ticketstatusMapper = ticketstatusMapper;
    }

    @Override
    public TicketstatusDTO save(TicketstatusDTO ticketstatusDTO) {
        log.debug("Request to save Ticketstatus : {}", ticketstatusDTO);
        Ticketstatus ticketstatus = ticketstatusMapper.toEntity(ticketstatusDTO);
        ticketstatus = ticketstatusRepository.save(ticketstatus);
        return ticketstatusMapper.toDto(ticketstatus);
    }

    @Override
    public TicketstatusDTO update(TicketstatusDTO ticketstatusDTO) {
        log.debug("Request to update Ticketstatus : {}", ticketstatusDTO);
        Ticketstatus ticketstatus = ticketstatusMapper.toEntity(ticketstatusDTO);
        ticketstatus = ticketstatusRepository.save(ticketstatus);
        return ticketstatusMapper.toDto(ticketstatus);
    }

    @Override
    public Optional<TicketstatusDTO> partialUpdate(TicketstatusDTO ticketstatusDTO) {
        log.debug("Request to partially update Ticketstatus : {}", ticketstatusDTO);

        return ticketstatusRepository
            .findById(ticketstatusDTO.getId())
            .map(existingTicketstatus -> {
                ticketstatusMapper.partialUpdate(existingTicketstatus, ticketstatusDTO);

                return existingTicketstatus;
            })
            .map(ticketstatusRepository::save)
            .map(ticketstatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketstatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ticketstatuses");
        return ticketstatusRepository.findAll(pageable).map(ticketstatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketstatusDTO> findOne(Long id) {
        log.debug("Request to get Ticketstatus : {}", id);
        return ticketstatusRepository.findById(id).map(ticketstatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticketstatus : {}", id);
        ticketstatusRepository.deleteById(id);
    }
}
