package com.vts.clientcenter.web.rest;

import com.vts.clientcenter.service.OrganizationService;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.service.dto.OrganizationDTO;
import com.vts.clientcenter.service.dto.OrganizationCriteria;
import com.vts.clientcenter.service.OrganizationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vts.clientcenter.domain.Organization}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

}
