package com.vts.clientcenter.service.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse {
    private String publicID;
    private long version;
    private String signature;
    private String resourceType;
    private Instant createdAt;
    private List<Object> tags;
    private long bytes;
    private String type;
    private String etag;
    private String url;
    private String secureURL;
    private String originalFilename;
}
