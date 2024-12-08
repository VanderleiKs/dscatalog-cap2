package com.devsuperior.dscatalog.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsers(Pageable page) {
        var users = userRepository.findAll(page);
        return new PageImpl<UserDTO>(
                users.stream().map(u -> new UserDTO(u)).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User obj = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(obj);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDTO) {
        final User user = new User(null,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()));

        userDTO.getRoles().forEach(r -> user.getRoles().add(
                roleRepository.getReferenceById(r.id())));

        var newUser = userRepository.save(user);

        return new UserDTO(newUser);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        user.setFirstName(dto.getFirstName()); 
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        user.getRoles().clear();

        dto.getRoles().forEach(r -> user.getRoles().add(
                roleRepository.getReferenceById(r.id())));

        var newUser = userRepository.save(user);

        return new UserDTO(newUser);

    }

    @Transactional
    public void delete(Long id) {
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("id not found");
        }
        try{
            userRepository.deleteById(id);
        }
        catch(DataIntegrityViolationException ex){
            throw new DatabaseException("user not can deleted");
        }
    }
}
