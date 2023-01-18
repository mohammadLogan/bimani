package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Repairshop;
import com.atlas.ir.repository.RepairshopRepository;
import com.atlas.ir.service.RepairshopService;
import com.atlas.ir.service.dto.RepairshopDTO;
import com.atlas.ir.service.mapper.RepairshopMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Repairshop}.
 */
@Service
@Transactional
public class RepairshopServiceImpl implements RepairshopService {

    private final Logger log = LoggerFactory.getLogger(RepairshopServiceImpl.class);

    private final RepairshopRepository repairshopRepository;

    private final RepairshopMapper repairshopMapper;

    public RepairshopServiceImpl(RepairshopRepository repairshopRepository, RepairshopMapper repairshopMapper) {
        this.repairshopRepository = repairshopRepository;
        this.repairshopMapper = repairshopMapper;
    }

    @Override
    public RepairshopDTO save(RepairshopDTO repairshopDTO) {
        log.debug("Request to save Repairshop : {}", repairshopDTO);
        Repairshop repairshop = repairshopMapper.toEntity(repairshopDTO);
        repairshop = repairshopRepository.save(repairshop);
        return repairshopMapper.toDto(repairshop);
    }

    @Override
    public RepairshopDTO update(RepairshopDTO repairshopDTO) {
        log.debug("Request to update Repairshop : {}", repairshopDTO);
        Repairshop repairshop = repairshopMapper.toEntity(repairshopDTO);
        repairshop = repairshopRepository.save(repairshop);
        return repairshopMapper.toDto(repairshop);
    }

    @Override
    public Optional<RepairshopDTO> partialUpdate(RepairshopDTO repairshopDTO) {
        log.debug("Request to partially update Repairshop : {}", repairshopDTO);

        return repairshopRepository
            .findById(repairshopDTO.getId())
            .map(existingRepairshop -> {
                repairshopMapper.partialUpdate(existingRepairshop, repairshopDTO);

                return existingRepairshop;
            })
            .map(repairshopRepository::save)
            .map(repairshopMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepairshopDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repairshops");
        return repairshopRepository.findAll(pageable).map(repairshopMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepairshopDTO> findOne(Long id) {
        log.debug("Request to get Repairshop : {}", id);
        return repairshopRepository.findById(id).map(repairshopMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Repairshop : {}", id);
        repairshopRepository.deleteById(id);
    }
}
