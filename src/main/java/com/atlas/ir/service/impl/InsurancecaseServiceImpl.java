package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Insurancecase;
import com.atlas.ir.repository.InsurancecaseRepository;
import com.atlas.ir.service.InsurancecaseService;
import com.atlas.ir.service.dto.InsurancecaseDTO;
import com.atlas.ir.service.mapper.InsurancecaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Insurancecase}.
 */
@Service
@Transactional
public class InsurancecaseServiceImpl implements InsurancecaseService {

    private final Logger log = LoggerFactory.getLogger(InsurancecaseServiceImpl.class);

    private final InsurancecaseRepository insurancecaseRepository;

    private final InsurancecaseMapper insurancecaseMapper;

    public InsurancecaseServiceImpl(InsurancecaseRepository insurancecaseRepository, InsurancecaseMapper insurancecaseMapper) {
        this.insurancecaseRepository = insurancecaseRepository;
        this.insurancecaseMapper = insurancecaseMapper;
    }

    @Override
    public InsurancecaseDTO save(InsurancecaseDTO insurancecaseDTO) {
        log.debug("Request to save Insurancecase : {}", insurancecaseDTO);
        Insurancecase insurancecase = insurancecaseMapper.toEntity(insurancecaseDTO);
        insurancecase = insurancecaseRepository.save(insurancecase);
        return insurancecaseMapper.toDto(insurancecase);
    }

    @Override
    public InsurancecaseDTO update(InsurancecaseDTO insurancecaseDTO) {
        log.debug("Request to update Insurancecase : {}", insurancecaseDTO);
        Insurancecase insurancecase = insurancecaseMapper.toEntity(insurancecaseDTO);
        insurancecase = insurancecaseRepository.save(insurancecase);
        return insurancecaseMapper.toDto(insurancecase);
    }

    @Override
    public Optional<InsurancecaseDTO> partialUpdate(InsurancecaseDTO insurancecaseDTO) {
        log.debug("Request to partially update Insurancecase : {}", insurancecaseDTO);

        return insurancecaseRepository
            .findById(insurancecaseDTO.getId())
            .map(existingInsurancecase -> {
                insurancecaseMapper.partialUpdate(existingInsurancecase, insurancecaseDTO);

                return existingInsurancecase;
            })
            .map(insurancecaseRepository::save)
            .map(insurancecaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsurancecaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Insurancecases");
        return insurancecaseRepository.findAll(pageable).map(insurancecaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsurancecaseDTO> findOne(Long id) {
        log.debug("Request to get Insurancecase : {}", id);
        return insurancecaseRepository.findById(id).map(insurancecaseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Insurancecase : {}", id);
        insurancecaseRepository.deleteById(id);
    }
}
