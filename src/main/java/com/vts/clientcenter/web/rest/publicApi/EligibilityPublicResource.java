package com.vts.clientcenter.web.rest.publicApi;

import com.google.zxing.WriterException;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.service.CloudinaryService;
import com.vts.clientcenter.service.EligibilityQueryService;
import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.service.dto.*;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link com.vts.clientcenter.domain.User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/public")
public class EligibilityPublicResource {
    private final Logger log = LoggerFactory.getLogger(EligibilityPublicResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EligibilityService eligibilityService;

    private final CloudinaryService cloudinaryService;

    private final EligibilityQueryService eligibilityQueryService;

    public EligibilityPublicResource(EligibilityService accountService, CloudinaryService cloudinaryService, EligibilityQueryService eligibilityQueryService) {
        this.eligibilityService = accountService;
        this.cloudinaryService = cloudinaryService;
        this.eligibilityQueryService = eligibilityQueryService;
    }

    @PostMapping("/eligibility/createAccount")
    public ResponseEntity<EligibilityDTO> createAccount(@Valid @RequestBody EligibilityCreationRequest dto) throws IOException, WriterException {
        EligibilityDTO result = eligibilityService.createEligibility(dto);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value="/eligibility/upload", method= RequestMethod.POST)
    public ResponseEntity<UploadFileResponse> uploadAttachmentFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        UploadFileResponse uploadFileResponse = cloudinaryService.uploadFileToCloud(multipartFile, Constants.FOLDER_ELIGIBILITY);

        return ResponseEntity.ok(uploadFileResponse);
    }

    @RequestMapping(value="/eligibility/receivedPresent", method= RequestMethod.POST)
    public ResponseEntity<EligibilityPresentStatusDTO> receivedPresent(@RequestBody ReceivedPresentDto dto) throws IOException {
        EligibilityPresentStatusDTO res = eligibilityService.receivedPresentCheck(dto);
        return ResponseEntity.accepted().body(res);

    }

    @GetMapping("/eligibility/list")
    public ResponseEntity<List<EligibilityDTO>> getListEligibility(EligibilityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get eligibility by criteria: {}", criteria);
        Page<EligibilityDTO> page = eligibilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /eligibilities/:id} : get the "id" eligibility.
     *
     * @param id the id of the eligibilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eligibilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eligibility/{id}")
    public ResponseEntity<EligibilityDetailDto> getEligibility(@PathVariable String id) {
        log.debug("REST request to get Eligibility : {}", id);
        EligibilityDetailDto res = eligibilityService.findByPrimaryId(id);
        return ResponseEntity.ok(res);
    }

}
