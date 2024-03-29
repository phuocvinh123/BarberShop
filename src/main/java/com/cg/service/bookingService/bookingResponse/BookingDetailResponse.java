package com.cg.service.bookingService.bookingResponse;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingDetailResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private LocalDateTime dayTimeBooking;
    private BigDecimal totalPrice;
    private List<Long> hairDetailIds;
    private Long stylistId;
    private String role;
}
