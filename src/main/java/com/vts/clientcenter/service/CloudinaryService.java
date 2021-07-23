package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public interface CloudinaryService {
    UploadFileResponse uploadFileToCloud(MultipartFile file, String folderName);
    UploadFileResponse uploadFileToCloudByBytes(byte[] file, String eligibilityId, String folderName);
}
