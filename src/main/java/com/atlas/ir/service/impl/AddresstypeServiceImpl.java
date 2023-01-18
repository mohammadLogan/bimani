package com.atlas.ir.service.impl;

import com.atlas.ir.domain.Addresstype;
import com.atlas.ir.repository.AddresstypeRepository;
import com.atlas.ir.service.AddresstypeService;
import com.atlas.ir.service.dto.AddresstypeDTO;
import com.atlas.ir.service.mapper.AddresstypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Addresstype}.
 */
@Service
@Transactional
public class AddresstypeServiceImpl implements AddresstypeService {

    private final Logger log = LoggerFactory.getLogger(AddresstypeServiceImpl.class);

    private final AddresstypeRepository addresstypeRepository;

    private final AddresstypeMapper addresstypeMapper;

    public AddresstypeServiceImpl(AddresstypeRepository addresstypeRepository, AddresstypeMapper addresstypeMapper) {
        this.addresstypeRepository = addresstypeRepository;
        this.addresstypeMapper = addresstypeMapper;
    }

    @Override
    public AddresstypeDTO save(AddresstypeDTO addresstypeDTO) {
        log.debug("Request to save Addresstype : {}", addresstypeDTO);
        Addresstype addresstype = addresstypeMapper.toEntity(addresstypeDTO);
        addresstype = addresstypeRepository.save(addresstype);
        return addresstypeMapper.toDto(addresstype);
    }

    @Override
    public AddresstypeDTO update(AddresstypeDTO addresstypeDTO) {
        log.debug("Request to update Addresstype : {}", addresstypeDTO);
        Addresstype addresstype = addresstypeMapper.toEntity(addresstypeDTO);
        addresstype = addresstypeRepository.save(addresstype);
        return addresstypeMapper.toDto(addresstype);
    }

    @Override
    public Optional<AddresstypeDTO> partialUpdate(AddresstypeDTO addresstypeDTO) {
        log.debug("Request to partially update Addresstype : {}", addresstypeDTO);

        return addresstypeRepository
            .findById(addresstypeDTO.getId())
            .map(existingAddresstype -> {
                addresstypeMapper.partialUpdate(existingAddresstype, addresstypeDTO);

                return existingAddresstype;
            })
            .map(addresstypeRepository::save)
            .map(addresstypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddresstypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresstypes");
        return addresstypeRepository.findAll(pageable).map(addresstypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AddresstypeDTO> findOne(Long id) {
        log.debug("Request to get Addresstype : {}", id);
        return addresstypeRepository.findById(id).map(addresstypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Addresstype : {}", id);
        addresstypeRepository.deleteById(id);
    }
}
