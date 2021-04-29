package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EmployerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.vts.clientcenter.domain.Employer}.
 */
public interface EmployerExtensionService {
    /**
     * Save a employer.
     *
     * @param employerDTO the entity to save.
     * @return the persisted entity.
     */
    EmployerDTO save(EmployerDTO employerDTO);
}
