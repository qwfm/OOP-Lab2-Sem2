package org.example.hotelbooking.service;

import lombok.RequiredArgsConstructor;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.entity.User;
import org.example.hotelbooking.mappers.UserMapper;
import org.example.hotelbooking.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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

        // **debug**: перевіримо, що дійшло вже в DTO
        System.out.printf(">>> dto.id=%s, dto.role=%s, dto.email=%s%n",
                dto.getId(), dto.getRole(), dto.getEmail());

        return dto;
    }
}
