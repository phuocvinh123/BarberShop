package com.cg.service.stylistService.stylistResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class StylistListResponse {
    private Long id;

    private String name;

    private String gender;

    private String status;

    private List<String> images;
}
