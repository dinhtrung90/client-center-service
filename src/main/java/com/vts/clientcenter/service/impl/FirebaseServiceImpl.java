package com.vts.clientcenter.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.vts.clientcenter.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Component
public class FirebaseServiceImpl implements FirebaseService {

    private final Logger log = LoggerFactory.getLogger(FirebaseServiceImpl.class);

    @Autowired
    private Environment environment;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        firebaseInitialize();
    }

    @Override
    public void firebaseInitialize() {
        try {
            ClassPathResource serviceAccount =
                new ClassPathResource(String.format("config/firebase/%s", Objects.requireNonNull(environment.getProperty("server-uploads.firebase.config-file"))));

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .setStorageBucket(environment.getProperty("server-uploads.firebase.bucket-url"))
                .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.debug("Firebase Init error: {}", e.getMessage());
        }

    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());
        BlobId blobId = BlobId.of(Objects.requireNonNull(environment.getProperty("server-uploads.firebase.bucket-name")), name);
        bucket.getStorage().createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }

    @Override
    public String downloadFile(String name) {
        return String.format("%s/%s/%s", Objects.requireNonNull(environment.getProperty("server-uploads.firebase.image-url")), Objects.requireNonNull(environment.getProperty("server-uploads.firebase.bucket-name")), name);
    }
}
