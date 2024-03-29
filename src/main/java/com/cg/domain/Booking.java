package com.cg.domain;

import com.cg.domain.Enum.EStatusBooking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dayTimeBooking;

    private String name;

    private String phoneNumber;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "booking")
    private List<BookingDetail> bookingDetails;

    @ManyToOne
    private Stylist stylist;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private EStatusBooking status;

}
