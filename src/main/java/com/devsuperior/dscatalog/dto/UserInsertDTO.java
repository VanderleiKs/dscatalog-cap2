package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validations.UserInsertValid;
import lombok.Getter;
import lombok.Setter;

@UserInsertValid()
public class UserInsertDTO extends UserDTO {
        @Getter @Setter
        private String password;

        public UserInsertDTO() {
                super();
        }

}
