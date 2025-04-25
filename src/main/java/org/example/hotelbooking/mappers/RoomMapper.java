package org.example.hotelbooking.mappers;

import org.example.hotelbooking.dto.RoomDTO;
import org.example.hotelbooking.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDTO toDto(Room entity);
    Room toEntity(RoomDTO dto);
}
