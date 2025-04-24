package org.example.hotelbooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.RoomDTO;
import org.example.hotelbooking.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> create(@RequestBody RoomDTO dto) {
        RoomDTO created = roomService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> listFree() {
        List<RoomDTO> rooms = roomService.getFreeRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getOne(@PathVariable Long id) {
        RoomDTO room = roomService.getById(id);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
