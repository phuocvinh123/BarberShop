package com.cg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private String name;

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private HairDetail hairDetail;

     public BookingDetail(Booking booking, HairDetail hairDetail,String name, BigDecimal price){
         this.booking = booking;
         this.hairDetail = hairDetail;
         this.name = name;
         this.price = price;
     }

}
