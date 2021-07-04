package com.vts.clientcenter.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vts.clientcenter.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientAppDto {

    private String id;

    private String name;

    private String desc;

    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();
}
