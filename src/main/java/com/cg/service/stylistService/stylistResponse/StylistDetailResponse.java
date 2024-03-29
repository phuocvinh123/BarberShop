package com.cg.service.stylistService.stylistResponse;

import com.cg.domain.Enum.EGender;
import com.cg.domain.Enum.EStatusStylist;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StylistDetailResponse {
    private Long id;

    private String name;

    private EGender gender;

    private EStatusStylist status;

    private List<String> images;
}
