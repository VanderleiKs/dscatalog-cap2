package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.InsertUserDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable page) {
        return ResponseEntity.ok(userService.findAllUsers(page));
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping()
    public UserDTO insertUser(@Valid @RequestBody InsertUserDTO entity) {
        return userService.insert(entity);
    }

    @PutMapping("/{id}")
    public UserDTO insertUser(@PathVariable Long id, @RequestBody UserDTO entity) {
        return userService.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
