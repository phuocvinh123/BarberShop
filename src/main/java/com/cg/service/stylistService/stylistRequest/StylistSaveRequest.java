package com.cg.service.stylistService.stylistRequest;

import com.cg.service.dto.request.SelectOptionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StylistSaveRequest {

    private String name;

    private String gender;

    private String status;

    private List<SelectOptionRequest> files;
}
