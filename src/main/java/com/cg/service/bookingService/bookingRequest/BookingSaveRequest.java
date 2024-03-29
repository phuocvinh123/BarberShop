package com.cg.service.bookingService.bookingRequest;

import com.cg.service.dto.request.SelectOptionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaveRequest {

    private String name;

    private String phoneNumber;

    private String dayBooking;

    private String timeBooking;

    private List<String> idHairDetails;

    private SelectOptionRequest stylist;

    private String idUser;

}
