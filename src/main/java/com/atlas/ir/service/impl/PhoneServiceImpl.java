package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Phone;
import com.atlas.ir.repository.PhoneRepository;
import com.atlas.ir.service.PhoneService;
import com.atlas.ir.service.dto.PhoneDTO;
import com.atlas.ir.service.mapper.PhoneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Phone}.
 */
@Service
@Transactional
public class PhoneServiceImpl implements PhoneService {

    private final Logger log = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper;

    public PhoneServiceImpl(PhoneRepository phoneRepository, PhoneMapper phoneMapper) {
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
    }

    @Override
    public PhoneDTO save(PhoneDTO phoneDTO) {
        log.debug("Request to save Phone : {}", phoneDTO);
        Phone phone = phoneMapper.toEntity(phoneDTO);
        phone = phoneRepository.save(phone);
        return phoneMapper.toDto(phone);
    }

    @Override
    public PhoneDTO update(PhoneDTO phoneDTO) {
        log.debug("Request to update Phone : {}", phoneDTO);
        Phone phone = phoneMapper.toEntity(phoneDTO);
        phone = phoneRepository.save(phone);
        return phoneMapper.toDto(phone);
    }

    @Override
    public Optional<PhoneDTO> partialUpdate(PhoneDTO phoneDTO) {
        log.debug("Request to partially update Phone : {}", phoneDTO);

        return phoneRepository
            .findById(phoneDTO.getId())
            .map(existingPhone -> {
                phoneMapper.partialUpdate(existingPhone, phoneDTO);

                return existingPhone;
            })
            .map(phoneRepository::save)
            .map(phoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Phones");
        return phoneRepository.findAll(pageable).map(phoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhoneDTO> findOne(Long id) {
        log.debug("Request to get Phone : {}", id);
        return phoneRepository.findById(id).map(phoneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Phone : {}", id);
        phoneRepository.deleteById(id);
    }
}
