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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private Cloudinary cloudinary;

    @Value("${server-uploads.cloudinary.cloud_name}")
    private String cloudName;

    @Value("${server-uploads.cloudinary.api_key}")
    private String apiKey;

    @Value("${server-uploads.cloudinary.api_secret}")
    private String apiSecret;


    private ObjectMapper objectMapper;

    @PostConstruct
    public void initialize() {
        log.info("initialize cloudinary");
        Map options = ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret);

        this.cloudinary = new Cloudinary(options);
    }

    @Override
    public UploadFileResponse uploadFileToCloud(MultipartFile file) {
        try {
            Map response = this.cloudinary.uploader().upload(file.getBytes(),  ObjectUtils.asMap("resource_type", "image"));

            return UploadFileResponse
                .builder()
                .originalFilename(response.get("original_filename").toString())
                .publicID(response.get("public_id").toString())
                .url(response.get("url").toString())
                .resourceType(response.get("resource_type").toString())
                .signature(response.get("signature").toString())
                .secureURL(response.get("secure_url").toString())
                .build();
        } catch (Exception e) {
            log.error("upload file with error: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), "CLOUDINARY_UPLOADS", "ERROR_UPLOAD_FILE");
        }
    }
}
