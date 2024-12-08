/* package com.devsuperior.dscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.InsertUserDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<InsertValid, InsertUserDTO>  {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(InsertUserDTO value, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        //Validações
        var user = userRepository.findByEmail(value.email());

        if(user.isPresent()){
            list.add(new FieldMessage("Email", "Email already exist"));
        }


        for(var e : list){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.field()).addConstraintViolation();
        }
      return list.isEmpty();
    }
    
}
 */