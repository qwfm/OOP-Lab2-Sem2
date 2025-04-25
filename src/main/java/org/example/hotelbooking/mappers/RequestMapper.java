package org.example.hotelbooking.mappers;

import org.example.hotelbooking.dto.BookingDTO;
import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(source = "id",    target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "roomType",  target = "roomType")
    @Mapping(source = "guests",  target = "guests")
    @Mapping(source = "status",  target = "status")
    @Mapping(source = "checkIn",  target = "checkIn")
    @Mapping(source = "checkOut",  target = "checkOut")
    RequestDTO toDto(Request request);
    Request toEntity(RequestDTO dto);
}
