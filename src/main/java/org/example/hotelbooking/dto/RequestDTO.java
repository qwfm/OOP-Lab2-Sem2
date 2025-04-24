package org.example.hotelbooking.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {
    private Long id;
    private Long clientId;
    private String roomType;
    private Integer guests;
    private String status;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
