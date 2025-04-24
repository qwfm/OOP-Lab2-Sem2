package org.example.hotelbooking.mappers;

import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}
