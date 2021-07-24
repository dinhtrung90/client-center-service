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
}
