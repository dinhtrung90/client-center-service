package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.PermissionOperation;
import com.vts.clientcenter.repository.PermissionOperationRepository;
import com.vts.clientcenter.service.PermissionOperationService;
import com.vts.clientcenter.service.dto.PermissionOperationDTO;
import com.vts.clientcenter.service.mapper.PermissionOperationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PermissionOperation}.
 */
@Service
@Transactional
public class PermissionOperationServiceImpl implements PermissionOperationService {
    private final Logger log = LoggerFactory.getLogger(PermissionOperationServiceImpl.class);

    private final PermissionOperationRepository permissionOperationRepository;

    private final PermissionOperationMapper permissionOperationMapper;

    public PermissionOperationServiceImpl(
        PermissionOperationRepository permissionOperationRepository,
        PermissionOperationMapper permissionOperationMapper
    ) {
        this.permissionOperationRepository = permissionOperationRepository;
        this.permissionOperationMapper = permissionOperationMapper;
    }

    @Override
    public PermissionOperationDTO save(PermissionOperationDTO permissionOperationDTO) {
        log.debug("Request to save PermissionOperation : {}", permissionOperationDTO);
        PermissionOperation permissionOperation = permissionOperationMapper.toEntity(permissionOperationDTO);
        permissionOperation = permissionOperationRepository.save(permissionOperation);
        return permissionOperationMapper.toDto(permissionOperation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionOperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PermissionOperations");
        return permissionOperationRepository.findAll(pageable).map(permissionOperationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionOperationDTO> findOne(Long id) {
        log.debug("Request to get PermissionOperation : {}", id);
        return permissionOperationRepository.findById(id).map(permissionOperationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PermissionOperation : {}", id);
        permissionOperationRepository.deleteById(id);
    }
}
