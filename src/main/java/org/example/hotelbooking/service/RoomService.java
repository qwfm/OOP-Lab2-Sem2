package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.RoomDTO;
import org.example.hotelbooking.entity.Room;
import org.example.hotelbooking.mappers.RoomMapper;
import org.example.hotelbooking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomDTO create(RoomDTO dto) {
        Room entity = roomMapper.toEntity(dto);
        entity.setStatus("FREE");
        Room saved = roomRepository.save(entity);
        return roomMapper.toDto(saved);
    }

    public List<RoomDTO> getAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public RoomDTO getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return roomMapper.toDto(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    public List<RoomDTO> getFreeRooms() {
        return roomRepository.findByStatus("FREE").stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public RoomDTO occupy(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus("OCCUPIED");
        return roomMapper.toDto(roomRepository.save(room));
    }

    public RoomDTO free(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus("FREE");
        return roomMapper.toDto(roomRepository.save(room));
    }
}
