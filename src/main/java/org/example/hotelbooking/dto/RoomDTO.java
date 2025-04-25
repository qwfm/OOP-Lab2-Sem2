package org.example.hotelbooking.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private String type;
    private Integer capacity;
    private BigDecimal pricePerNight;
    private String status;
}