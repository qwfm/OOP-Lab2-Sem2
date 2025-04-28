package org.example.hotelbooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.service.RequestService;
import org.example.hotelbooking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RequestDTO> create(
            @RequestBody RequestDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        dto.setClientId(user.getId());
        RequestDTO created = requestService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RequestDTO>> listAll(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        System.out.printf(">>> user.id=%s, user.role=%s%n", user.getId(), user.getRole());
        List<RequestDTO> list =
                "admin".equalsIgnoreCase(user.getRole())
                        ? requestService.getAll()
                        : requestService.getByClient(user.getId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        RequestDTO req = requestService.getAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!"admin".equalsIgnoreCase(user.getRole())
                && !req.getClientId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(req);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<RequestDTO> confirmRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        RequestDTO dto = requestService.confirm(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<RequestDTO> rejectRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        RequestDTO dto = requestService.reject(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RequestDTO>> listAllRequests(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UserDTO user = userService.findOrCreate(jwt);
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(requestService.getAll());
    }
}
