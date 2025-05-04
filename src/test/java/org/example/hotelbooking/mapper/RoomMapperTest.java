package org.example.hotelbooking.mapper;

import org.example.hotelbooking.dto.RoomDTO;
import org.example.hotelbooking.entity.Room;
import org.example.hotelbooking.mappers.RoomMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomMapperTest {
    private final RoomMapper mapper = Mappers.getMapper(RoomMapper.class);

    @Test
    void toDto_and_back_entityFieldsMatch() {
        Room entity = Room.builder()
                .id(1L)
                .type("Standard")
                .capacity(2)
                .pricePerNight(new java.math.BigDecimal("150.00"))
                .status("FREE")
                .build();

        RoomDTO dto = mapper.toDto(entity);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getType(), dto.getType());
        assertEquals(entity.getCapacity(), dto.getCapacity());
        assertEquals(entity.getPricePerNight(), dto.getPricePerNight());
        assertEquals(entity.getStatus(), dto.getStatus());

        Room back = mapper.toEntity(dto);
        assertEquals(dto.getId(), back.getId());
        assertEquals(dto.getType(), back.getType());
        assertEquals(dto.getCapacity(), back.getCapacity());
        assertEquals(dto.getPricePerNight(), back.getPricePerNight());
        assertEquals(dto.getStatus(), back.getStatus());
    }
}
