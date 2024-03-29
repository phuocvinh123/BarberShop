package com.cg.service.bookingService.bookingResponse;

import lombok.*;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingListResponse {
    private Long id;

    private String name;

    private String phoneNumber;

    private BigDecimal totalPrice;

    private String dayTimeBooking;

    private String stylist;

    private String bookingDetails;

    private String status;

    private String role;
}
