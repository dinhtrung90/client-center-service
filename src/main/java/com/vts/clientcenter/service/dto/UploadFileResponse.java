package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse implements Serializable {

    @JsonProperty("public_id")
    private String publicID;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("resource_type")
    private String resourceType;

    private String url;
    @JsonProperty("secure_url")
    private String secureURL;

    @JsonProperty("original_filename")
    private String originalFilename;
}
