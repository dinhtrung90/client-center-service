package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.UserProfileQueryService;
import com.vts.clientcenter.service.UserProfileService;
import com.vts.clientcenter.service.dto.UserProfileCriteria;
import com.vts.clientcenter.service.dto.UserProfileDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.UserProfile}.
 */
@RestController
@RequestMapping("/api")
public class UserProfileResource {
    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);
}
