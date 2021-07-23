package com.vts.clientcenter.service.impl;

import com.google.zxing.WriterException;
import com.vts.clientcenter.domain.EligibilityMetadata;
import com.vts.clientcenter.domain.EligibilityPresentStatus;
import com.vts.clientcenter.helpers.QRCodeGenerator;
import com.vts.clientcenter.repository.EligibilityMetadataRepository;
import com.vts.clientcenter.service.CloudinaryService;
import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.repository.EligibilityRepository;
import com.vts.clientcenter.service.dto.EligibilityCreationRequest;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;
import com.vts.clientcenter.service.dto.UploadFileResponse;
import com.vts.clientcenter.service.mapper.EligibilityMapper;
import com.vts.clientcenter.service.mapper.EligibilityMetadataMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import jodd.util.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.vts.clientcenter.config.Constants.*;

/**
 * Service Implementation for managing {@link Eligibility}.
 */
@Service
@Transactional
public class EligibilityServiceImpl implements EligibilityService {

    private final Logger log = LoggerFactory.getLogger(EligibilityServiceImpl.class);

    private final EligibilityRepository eligibilityRepository;

    private final EligibilityMapper eligibilityMapper;

    private final EligibilityMetadataMapper eligibilityMetadataMapper;

    private final EligibilityMetadataRepository eligibilityMetadataRepository;

    private final QRCodeGenerator qrCodeGenerator;

    private final CloudinaryService cloudinaryService;

    public EligibilityServiceImpl(EligibilityRepository eligibilityRepository, EligibilityMapper eligibilityMapper, EligibilityMetadataMapper eligibilityMetadataMapper, EligibilityMetadataRepository eligibilityMetadataRepository, QRCodeGenerator qrCodeGenerator, CloudinaryService cloudinaryService) {
        this.eligibilityRepository = eligibilityRepository;
        this.eligibilityMapper = eligibilityMapper;
        this.eligibilityMetadataMapper = eligibilityMetadataMapper;
        this.eligibilityMetadataRepository = eligibilityMetadataRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public EligibilityDTO save(EligibilityDTO eligibilityDTO) {
        log.debug("Request to save Eligibility : {}", eligibilityDTO);
        Eligibility eligibility = eligibilityMapper.toEntity(eligibilityDTO);
        eligibility = eligibilityRepository.save(eligibility);
        return eligibilityMapper.toDto(eligibility);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EligibilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Eligibilities");
        return eligibilityRepository.findAll(pageable)
            .map(eligibilityMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EligibilityDTO> findOne(String id) {
        log.debug("Request to get Eligibility : {}", id);
        return eligibilityRepository.findById(id)
            .map(eligibilityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Eligibility : {}", id);
        eligibilityRepository.deleteById(id);
    }

    @Transactional
    @Override
    public EligibilityDTO createEligibility(EligibilityCreationRequest dto) throws IOException, WriterException {

        if (Objects.isNull(dto.getEligibilityDTO())) {
            throw new BadRequestAlertException("Can not create Account", "Account", USER_CAN_NOT_CREATED);
        };

        EligibilityDTO eligibilityDTO = dto.getEligibilityDTO();

        Optional<Eligibility> eligibilityOptional = eligibilityRepository.findByEmail(eligibilityDTO.getEmail());
        if (eligibilityOptional.isPresent()) {
            throw new BadRequestAlertException("Email has existed.", "Account", EMAIL_HAS_EXISTED);
        }

        Optional<Eligibility> eligibilityPhoneOptional = eligibilityRepository.findByPhone(eligibilityDTO.getPhone());

        if (eligibilityPhoneOptional.isPresent()) {
            throw new BadRequestAlertException("Phone has existed.", "Account", PHONE_HAS_EXISTED);
        }

        Eligibility eligibility = eligibilityMapper.toEntity(eligibilityDTO);

        if (!CollectionUtils.isEmpty(dto.getEligibilityMetadata())) {
            List<EligibilityMetadataDTO> eligibilityMetadata = dto.getEligibilityMetadata();

            List<EligibilityMetadata> eligibilityMetadataList = eligibilityMetadataMapper.toEntity(eligibilityMetadata);

            for (EligibilityMetadata metadata : eligibilityMetadataList) {
                eligibility.addEligibilityMetadata(metadata);
            }
        }

        //generate present progress
        //currently 1-1
        EligibilityPresentStatus presentStatus = createPresentStatus(eligibility);

        eligibility.addEligibilityPresentStatus(presentStatus);

        eligibility = eligibilityRepository.save(eligibility);

        EligibilityDTO result = eligibilityMapper.toDto(eligibility);
        result.setExpiredDate(presentStatus.getExpiredAt());
        result.setCode(presentStatus.getCode());
        return result;
    }

    private EligibilityPresentStatus createPresentStatus(Eligibility eligibility) throws IOException, WriterException {

        Instant expiredAt = Instant.now().plus(EXPIRED_DAY_CODE, ChronoUnit.DAYS);
        String code = RandomString.get().randomAlphaNumeric(6);

        byte[] qrCodeImage = qrCodeGenerator.getQRCodeImage(code, QRCODE_WIDTH, QRCODE_HEIGHT);

        UploadFileResponse uploadFileResponse = cloudinaryService.uploadFileToCloudByBytes(qrCodeImage);
        return new EligibilityPresentStatus()
            .eligibility(eligibility)
            .hasPresent(false)
            .code(code)
            .expiredAt(expiredAt)
            .qrCodeUrl(Objects.isNull(uploadFileResponse.getUrl()) ? null: uploadFileResponse.getUrl());

    }
}
