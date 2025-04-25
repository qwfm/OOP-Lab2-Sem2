package org.example.hotelbooking.mappers;

import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id",    target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name",  target = "name")
    @Mapping(source = "role",  target = "role")
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}
