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
    @Mapping(source = "id",    target = "id")
    @Mapping(source = "requestId",    target = "requestId")
    @Mapping(source = "roomId",    target = "roomId")
    @Mapping(source = "totalPrice",    target = "totalPrice")
    @Mapping(source = "clientId",    target = "clientId")
    @Mapping(source = "checkIn",    target = "checkIn")
    @Mapping(source = "checkOut",    target = "checkOut")
    BookingDTO toDto(Booking entity);
    Booking toEntity(BookingDTO dto);
}
