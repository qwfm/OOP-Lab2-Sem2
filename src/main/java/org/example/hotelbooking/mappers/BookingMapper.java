package org.example.hotelbooking.mappers;

import org.example.hotelbooking.dto.BookingDTO;
import org.example.hotelbooking.entity.Booking;
import org.example.hotelbooking.entity.Room;
import org.example.hotelbooking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDTO toDto(Booking entity);
    Booking toEntity(BookingDTO dto);
}
