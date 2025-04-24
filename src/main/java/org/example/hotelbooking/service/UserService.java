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
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name("new user")
                                .role("client")
                                .build()
                ));
        return userMapper.toDto(user);
    }
}
