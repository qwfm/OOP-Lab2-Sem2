package org.example.hotelbooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal Jwt jwt) {
        UserDTO user = userService.findOrCreate(jwt);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> byId(@PathVariable Long id) {
        UserDTO user = userService.getById(id);
        return ResponseEntity.ok(user);
    }
}
