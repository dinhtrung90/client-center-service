package com.vts.clientcenter.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vts.clientcenter.service.CloudinaryService;
import com.vts.clientcenter.service.dto.UploadFileResponse;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.cloudinary.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private Cloudinary cloudinary;

    @Value("server-uploads.cloud_name")
    private String cloudName;

    @Value("server-uploads.api_key")
    private String apiKey;

    @Value("server-uploads.api_secret")
    private String apiSecret;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void initialize() {
        log.info("initialize cloudinary");
        Map options = ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret);

        this.cloudinary = new Cloudinary(options);
    }

    @Override
    public UploadFileResponse uploadFileToCloud(File file, Map<String, Object> metadata) {
        try {
            Map response = this.cloudinary.uploader().upload(file, metadata);
            JSONObject jsonObject = new JSONObject(response);
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(jsonObject), UploadFileResponse.class);
            } catch (Exception e) {
                log.error("Invalid JSON: {}", jsonObject);
                FileUtils.deleteQuietly(file);
                throw new BadRequestAlertException(e.getMessage(), "JSON", "ERROR_PARSE_JSON");
            }
        } catch (Exception e) {
            log.error("upload file with error: {}", e.getMessage());
            FileUtils.deleteQuietly(file);
            throw new BadRequestAlertException(e.getMessage(), "CLOUDINARY_UPLOADS", "ERROR_UPLOAD_FILE");
        }
    }
}
