package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.ModuleOperation;
import com.vts.clientcenter.repository.ModuleOperationRepository;
import com.vts.clientcenter.service.ModuleOperationService;
import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import com.vts.clientcenter.service.mapper.ModuleOperationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ModuleOperation}.
 */
@Service
@Transactional
public class ModuleOperationServiceImpl implements ModuleOperationService {
    private final Logger log = LoggerFactory.getLogger(ModuleOperationServiceImpl.class);

    private final ModuleOperationRepository moduleOperationRepository;

    private final ModuleOperationMapper moduleOperationMapper;

    public ModuleOperationServiceImpl(ModuleOperationRepository moduleOperationRepository, ModuleOperationMapper moduleOperationMapper) {
        this.moduleOperationRepository = moduleOperationRepository;
        this.moduleOperationMapper = moduleOperationMapper;
    }

    @Override
    public ModuleOperationDTO save(ModuleOperationDTO moduleOperationDTO) {
        log.debug("Request to save ModuleOperation : {}", moduleOperationDTO);
        ModuleOperation moduleOperation = moduleOperationMapper.toEntity(moduleOperationDTO);
        moduleOperation = moduleOperationRepository.save(moduleOperation);
        return moduleOperationMapper.toDto(moduleOperation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleOperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ModuleOperations");
        return moduleOperationRepository.findAll(pageable).map(moduleOperationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModuleOperationDTO> findOne(Long id) {
        log.debug("Request to get ModuleOperation : {}", id);
        return moduleOperationRepository.findById(id).map(moduleOperationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ModuleOperation : {}", id);
        moduleOperationRepository.deleteById(id);
    }
}
