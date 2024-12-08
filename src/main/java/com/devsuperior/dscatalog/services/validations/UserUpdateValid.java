package com.devsuperior.dscatalog.services.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

 // ----------------- Annotation ------------------------
/**
 * - Valida Update usuário
 */
// Permite passar um validador específico
@Constraint(validatedBy = UserUpdateValidator.class)
// Onde é permtida
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUpdateValid {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

 // ----------------- Validator ------------------------
class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO>  {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(UserUpdateDTO value, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        var userUri = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        var userId = Long.parseLong(userUri.get("id"));

        //Validações
        var user = userRepository.findByEmail(value.getEmail());

        if(user.isPresent() && user.get().getId() != userId){
            list.add(new FieldMessage("Email", "Email already exist"));
        }


        for(var e : list){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.message()).addPropertyNode(e.field()).addConstraintViolation();
        }
      return list.isEmpty();
    }
    
}
