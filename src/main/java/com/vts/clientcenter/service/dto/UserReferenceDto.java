package com.vts.clientcenter.service.dto;


import java.net.URI;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class UserReferenceDto {
    private static final String UNKNOWN_ID = "00000000-0000-0000-0000-000000000000";

    private String userId;

    public UserReferenceDto(@NonNull URI loc) {
        this(extractUserId(loc));
    }

    public UserReferenceDto(@NonNull String userId) {
        this.userId = userId;
    }

    private static String extractUserId(URI uri) {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}

