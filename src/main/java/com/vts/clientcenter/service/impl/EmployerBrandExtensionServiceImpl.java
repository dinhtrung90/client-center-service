package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.EmployerBrand;
import com.vts.clientcenter.repository.EmployerBrandRepository;
import com.vts.clientcenter.service.EmployerBrandExtensionService;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;
import com.vts.clientcenter.service.mapper.EmployerBrandMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployerBrandExtensionServiceImpl implements EmployerBrandExtensionService {

    @Autowired
    private EmployerBrandRepository employerBrandRepository;

    @Autowired
    private EmployerBrandMapper employerBrandMapper;

    @Override
    public EmployerBrandDTO save(EmployerBrandDTO dto) {

        if (dto.getEmployerId() == null) {
            throw new BadRequestAlertException("EmployerId can not null", "BRAND", "EMPLOYER_ID_NULL");
        }

        EmployerBrand employerBrand = employerBrandMapper.toEntity(dto);

        return employerBrandMapper.toDto(employerBrand);
    }
}
