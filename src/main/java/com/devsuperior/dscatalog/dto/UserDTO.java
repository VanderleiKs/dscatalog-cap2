package com.devsuperior.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.devsuperior.dscatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @Size(min = 3, max = 60, message = "Name invalid")
    private String firstName;

    @Size(min = 3, max = 60, message = "Name invalid")
    private String lastName;

    @Email(message = "Email invalid")
    private String email;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        entity.getRoles().forEach(r -> addRole(new RoleDTO(r.getId(), r.getAuthority())));
    }

    protected void addRole(RoleDTO role){
        this.roles.add(role);
    }
}
