package com.devsuperior.dscatalog.services.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.InsertUserDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

 // ----------------- Annotation ------------------------
/**
 * - Valida cadastro usuário
 */
// Permite passar um validador específico
@Constraint(validatedBy = UserInsertValidator.class)
// Onde é permtida
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInsertValid {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

 // ----------------- Validator ------------------------
class UserInsertValidator implements ConstraintValidator<UserInsertValid, InsertUserDTO>  {

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
