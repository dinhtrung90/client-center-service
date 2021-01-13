package com.vts.clientcenter.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.repository.EligibilityRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.TResult;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.service.CloudinaryService;
import com.vts.clientcenter.service.dto.UploadFileResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping("/api")
public class JobResource {

	private final JobLauncher jobLauncher;
	private final Job job;

	private final CloudinaryService cloudinaryService;

	private final EligibilityRepository eligibilityRepository;

	public JobResource(JobLauncher jobLauncher, Job job, CloudinaryService cloudinaryService, EligibilityRepository eligibilityRepository) {
		this.jobLauncher = jobLauncher;
		this.job = job;
        this.cloudinaryService = cloudinaryService;
        this.eligibilityRepository = eligibilityRepository;
    }

	/**
	 * POST /job/:fileName : post to execute a job using the file name given
	 *
	 * @param fileName
	 *            the fileName of the job file to run
	 * @return the ResponseEntity with status 200 (OK) or status 500 (Job Failure)
	 */
	@GetMapping("/{fileName:.+}")
	public ResponseEntity<String> processEmployeeCsv(@PathVariable String fileName) {
		Map<String, JobParameter> parameterMap = new HashMap<>();
		parameterMap.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter(fileName));
		try {
			jobLauncher.run(job, new JobParameters(parameterMap));
		} catch (Exception e) {
			return new ResponseEntity<String>("Failure: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

    @RequestMapping(value="employees/import-file/upload", method= RequestMethod.POST)
	public ResponseEntity uploadEmployeeCsv(@RequestParam("file") MultipartFile multipartFile) throws IOException {

	    // store file to s3
        File f= Files.createTempFile("temp", multipartFile.getOriginalFilename()).toFile();
        multipartFile.transferTo(f);

        UploadFileResponse uploadFileResponse = cloudinaryService.uploadFileToCloud(f, ObjectUtils.emptyMap());
        if (uploadFileResponse != null) {
            // store metadata db
            String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseGet(() -> "anonymous");

            Eligibility eligibility = new Eligibility()
                .createdBy(currentUserLogin)
                .createdDate(Instant.now())
                .fileName(multipartFile.getName())
                .fileUrl(uploadFileResponse.getUrl())
                .lastModifiedBy(currentUserLogin)
                .lastModifiedDate(Instant.now())
                .refId(uploadFileResponse.getSignature());
            eligibilityRepository.save(eligibility);
        }

        //process import

        // create response
        TResult result = TResult.builder()
            .result("Uploading")
            .isSuccess(true)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
