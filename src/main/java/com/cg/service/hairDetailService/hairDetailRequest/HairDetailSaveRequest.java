package com.cg.service.hairDetailService.hairDetailRequest;

import com.cg.service.dto.request.SelectOptionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HairDetailSaveRequest {
    private String name;

    private String description;

    private String price;

    private List<SelectOptionRequest> files;
}
