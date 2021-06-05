package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.service.UserAddressService;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.repository.UserAddressRepository;
import com.vts.clientcenter.service.dto.UserAddressDTO;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserAddress}.
 */
@Service
@Transactional
public class UserAddressServiceImpl implements UserAddressService {

    private final Logger log = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressServiceImpl(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    @Override
    public UserAddressDTO save(UserAddressDTO userAddressDTO) {
        log.debug("Request to save UserAddress : {}", userAddressDTO);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDTO);
        userAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toDto(userAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAddresses");
        return userAddressRepository.findAll(pageable)
            .map(userAddressMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<UserAddressDTO> findOne(Long id) {
        log.debug("Request to get UserAddress : {}", id);
        return userAddressRepository.findById(id)
            .map(userAddressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAddress : {}", id);
        userAddressRepository.deleteById(id);
    }
}
