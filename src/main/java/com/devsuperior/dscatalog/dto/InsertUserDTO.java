package com.devsuperior.dscatalog.dto;

import java.util.Set;

import com.devsuperior.dscatalog.services.validations.UserInsertValid;
import jakarta.validation.constraints.Size;

@UserInsertValid()
public record InsertUserDTO(
        @Size(min = 3, max = 60, message = "Name invalid") String firstName,
        String lastName,
        String email,
        String password,
        Set<RoleDTO> roles) {
}
