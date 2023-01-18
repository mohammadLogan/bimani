package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Insurance;
import com.atlas.ir.repository.InsuranceRepository;
import com.atlas.ir.service.InsuranceService;
import com.atlas.ir.service.dto.InsuranceDTO;
import com.atlas.ir.service.mapper.InsuranceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Insurance}.
 */
@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final Logger log = LoggerFactory.getLogger(InsuranceServiceImpl.class);

    private final InsuranceRepository insuranceRepository;

    private final InsuranceMapper insuranceMapper;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository, InsuranceMapper insuranceMapper) {
        this.insuranceRepository = insuranceRepository;
        this.insuranceMapper = insuranceMapper;
    }

    @Override
    public InsuranceDTO save(InsuranceDTO insuranceDTO) {
        log.debug("Request to save Insurance : {}", insuranceDTO);
        Insurance insurance = insuranceMapper.toEntity(insuranceDTO);
        insurance = insuranceRepository.save(insurance);
        return insuranceMapper.toDto(insurance);
    }

    @Override
    public InsuranceDTO update(InsuranceDTO insuranceDTO) {
        log.debug("Request to update Insurance : {}", insuranceDTO);
        Insurance insurance = insuranceMapper.toEntity(insuranceDTO);
        insurance = insuranceRepository.save(insurance);
        return insuranceMapper.toDto(insurance);
    }

    @Override
    public Optional<InsuranceDTO> partialUpdate(InsuranceDTO insuranceDTO) {
        log.debug("Request to partially update Insurance : {}", insuranceDTO);

        return insuranceRepository
            .findById(insuranceDTO.getId())
            .map(existingInsurance -> {
                insuranceMapper.partialUpdate(existingInsurance, insuranceDTO);

                return existingInsurance;
            })
            .map(insuranceRepository::save)
            .map(insuranceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Insurances");
        return insuranceRepository.findAll(pageable).map(insuranceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceDTO> findOne(Long id) {
        log.debug("Request to get Insurance : {}", id);
        return insuranceRepository.findById(id).map(insuranceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Insurance : {}", id);
        insuranceRepository.deleteById(id);
    }
}
