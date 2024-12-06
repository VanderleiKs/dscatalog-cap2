package com.devsuperior.dscatalog.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.devsuperior.dscatalog.entities.User;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Set<RoleDTO> roles) {
    public UserDTO(User entity) {
        this(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getRoles().stream().map(r -> new RoleDTO(r.getId(), r.getAuthority()))
                        .collect(Collectors.toSet()));
    }

}
