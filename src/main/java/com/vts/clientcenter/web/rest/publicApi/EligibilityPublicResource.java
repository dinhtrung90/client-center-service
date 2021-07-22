package com.vts.clientcenter.web.rest.publicApi;

import com.vts.clientcenter.service.CloudinaryService;
import com.vts.clientcenter.service.EligibilityService;
import com.vts.clientcenter.service.dto.EligibilityCreationRequest;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import com.vts.clientcenter.service.dto.UploadFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public EligibilityPublicResource(EligibilityService accountService, CloudinaryService cloudinaryService) {
        this.eligibilityService = accountService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/eligibility/createAccount")
    public ResponseEntity<EligibilityDTO> createAccount(@Valid @RequestBody EligibilityCreationRequest dto) {
        EligibilityDTO result = eligibilityService.createEligibility(dto);
        return ResponseEntity.ok(result);
    }


    @RequestMapping(value="/eligibility/upload", method= RequestMethod.POST)
    public ResponseEntity<UploadFileResponse> uploadAttachmentFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        UploadFileResponse uploadFileResponse = cloudinaryService.uploadFileToCloud(multipartFile);

        return ResponseEntity.ok(uploadFileResponse);

    }

}
