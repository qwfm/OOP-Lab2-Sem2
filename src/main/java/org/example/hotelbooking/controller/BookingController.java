package org.example.hotelbooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.BookingDTO;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.service.BookingService;
import org.example.hotelbooking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BookingDTO> create(
            @RequestBody BookingDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        dto.setClientId(user.getId());
        BookingDTO created = bookingService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> listAll(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        List<BookingDTO> list =
                "admin".equalsIgnoreCase(user.getRole())
                        ? bookingService.getAll()
                        : bookingService.getByClient(user.getId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        BookingDTO b = bookingService.getAll().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!"admin".equalsIgnoreCase(user.getRole())
                && !b.getClientId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        BookingDTO b = bookingService.getAll().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!"admin".equalsIgnoreCase(user.getRole())
                && !b.getClientId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
