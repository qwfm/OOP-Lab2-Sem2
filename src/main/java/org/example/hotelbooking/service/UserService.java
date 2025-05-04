package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.entity.User;
import org.example.hotelbooking.mappers.UserMapper;
import org.example.hotelbooking.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO findOrCreate(Jwt jwt) {
        String email = jwt.getClaim("email");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name("new user")
                            .role("client")
                            .build();
                    return userRepository.save(newUser);
                });
        System.out.printf(">>> entity.id=%s, entity.role=%s, entity.email=%s%n",
                user.getId(), user.getRole(), user.getEmail());

        UserDTO dto = userMapper.toDto(user);

        System.out.printf(">>> dto.id=%s, dto.role=%s, dto.email=%s%n",
                dto.getId(), dto.getRole(), dto.getEmail());

        return dto;
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id)
                );
    }
}
