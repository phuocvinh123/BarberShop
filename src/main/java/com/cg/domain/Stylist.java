package com.cg.domain;

import com.cg.domain.Enum.EGender;
import com.cg.domain.Enum.EStatusStylist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "stylists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stylist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @OneToMany(mappedBy = "stylist")
    private List<StylistImage> stylistImage;

    @Enumerated(EnumType.STRING)
    private EStatusStylist status;

}
