package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.RolePermission;
import com.vts.clientcenter.repository.RolePermissionRepository;
import com.vts.clientcenter.service.RolePermissionService;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import com.vts.clientcenter.service.mapper.RolePermissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RolePermission}.
 */
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {
    private final Logger log = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public RolePermissionDTO save(RolePermissionDTO rolePermissionDTO) {
        log.debug("Request to save RolePermission : {}", rolePermissionDTO);
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toDto(rolePermission);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RolePermissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RolePermissions");
        return rolePermissionRepository.findAll(pageable).map(rolePermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolePermissionDTO> findOne(Long id) {
        log.debug("Request to get RolePermission : {}", id);
        return rolePermissionRepository.findById(id).map(rolePermissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RolePermission : {}", id);
        rolePermissionRepository.deleteById(id);
    }
}
