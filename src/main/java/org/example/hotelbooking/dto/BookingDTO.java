package org.example.hotelbooking.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private Long requestId;
    private Long roomId;
    private BigDecimal totalPrice;
    private Long clientId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
