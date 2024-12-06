package com.devsuperior.dscatalog.dto;

import java.util.Set;

public record InsertUserDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        Set<RoleDTO> roles
        ) {
}
