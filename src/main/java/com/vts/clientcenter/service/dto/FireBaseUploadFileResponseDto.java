package com.vts.clientcenter.service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireBaseUploadFileResponseDto {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
