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
    RequestDTO toDto(Request request);
    Request toEntity(RequestDTO dto);
}
