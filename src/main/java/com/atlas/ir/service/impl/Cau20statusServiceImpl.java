package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Cau20status;
import com.atlas.ir.repository.Cau20statusRepository;
import com.atlas.ir.service.Cau20statusService;
import com.atlas.ir.service.dto.Cau20statusDTO;
import com.atlas.ir.service.mapper.Cau20statusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cau20status}.
 */
@Service
@Transactional
public class Cau20statusServiceImpl implements Cau20statusService {

    private final Logger log = LoggerFactory.getLogger(Cau20statusServiceImpl.class);

    private final Cau20statusRepository cau20statusRepository;

    private final Cau20statusMapper cau20statusMapper;

    public Cau20statusServiceImpl(Cau20statusRepository cau20statusRepository, Cau20statusMapper cau20statusMapper) {
        this.cau20statusRepository = cau20statusRepository;
        this.cau20statusMapper = cau20statusMapper;
    }

    @Override
    public Cau20statusDTO save(Cau20statusDTO cau20statusDTO) {
        log.debug("Request to save Cau20status : {}", cau20statusDTO);
        Cau20status cau20status = cau20statusMapper.toEntity(cau20statusDTO);
        cau20status = cau20statusRepository.save(cau20status);
        return cau20statusMapper.toDto(cau20status);
    }

    @Override
    public Cau20statusDTO update(Cau20statusDTO cau20statusDTO) {
        log.debug("Request to update Cau20status : {}", cau20statusDTO);
        Cau20status cau20status = cau20statusMapper.toEntity(cau20statusDTO);
        cau20status = cau20statusRepository.save(cau20status);
        return cau20statusMapper.toDto(cau20status);
    }

    @Override
    public Optional<Cau20statusDTO> partialUpdate(Cau20statusDTO cau20statusDTO) {
        log.debug("Request to partially update Cau20status : {}", cau20statusDTO);

        return cau20statusRepository
            .findById(cau20statusDTO.getId())
            .map(existingCau20status -> {
                cau20statusMapper.partialUpdate(existingCau20status, cau20statusDTO);

                return existingCau20status;
            })
            .map(cau20statusRepository::save)
            .map(cau20statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cau20statusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cau20statuses");
        return cau20statusRepository.findAll(pageable).map(cau20statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cau20statusDTO> findOne(Long id) {
        log.debug("Request to get Cau20status : {}", id);
        return cau20statusRepository.findById(id).map(cau20statusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cau20status : {}", id);
        cau20statusRepository.deleteById(id);
    }
}
