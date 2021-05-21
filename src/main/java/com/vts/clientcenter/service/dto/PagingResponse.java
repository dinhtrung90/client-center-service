package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse<T> {

    private int size;
    private int totalPage;
    private List<T> items;
}
