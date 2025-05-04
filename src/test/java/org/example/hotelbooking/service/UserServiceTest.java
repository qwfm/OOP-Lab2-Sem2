package org.example.hotelbooking.service;

import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.entity.User;
import org.example.hotelbooking.mappers.UserMapper;
import org.example.hotelbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findOrCreate_existingUser_returnsDto() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("email")).thenReturn("foo@example.com");

        User existing = User.builder()
                .id(1L)
                .email("foo@example.com")
                .name("Foo")
                .role("client")
                .build();
        when(userRepo.findByEmail("foo@example.com")).thenReturn(Optional.of(existing));

        UserDTO dto = UserDTO.builder()
                .id(1L)
                .email("foo@example.com")
                .name("Foo")
                .role("client")
                .build();
        when(userMapper.toDto(existing)).thenReturn(dto);

        UserDTO result = userService.findOrCreate(jwt);

        assertEquals(dto, result);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void findOrCreate_newUser_savedAndReturned() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("email")).thenReturn("bar@example.com");

        when(userRepo.findByEmail("bar@example.com")).thenReturn(Optional.empty());

        // Ми очікуємо, що UserService викличе save(...) з User.email="bar@example.com"
        User saved = User.builder()
                .id(2L)
                .email("bar@example.com")
                .name("new user")
                .role("client")
                .build();
        when(userRepo.save(argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(User u) {
                return "bar@example.com".equals(u.getEmail())
                        && "client".equals(u.getRole());
            }
        }))).thenReturn(saved);

        UserDTO dto = UserDTO.builder()
                .id(2L)
                .email("bar@example.com")
                .name("new user")
                .role("client")
                .build();
        when(userMapper.toDto(saved)).thenReturn(dto);

        UserDTO result = userService.findOrCreate(jwt);

        assertEquals(2L, result.getId());
        verify(userRepo).save(any(User.class));
    }
}
