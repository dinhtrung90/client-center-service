package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.UserProfile;
import com.vts.clientcenter.repository.UserProfileRepository;
import com.vts.clientcenter.service.UserProfileService;
import com.vts.clientcenter.service.dto.UserProfileDTO;
import com.vts.clientcenter.service.mapper.UserProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll(pageable).map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> findOne(String id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id).map(userProfileMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }
}
