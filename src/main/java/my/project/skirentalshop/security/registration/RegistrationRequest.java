package my.project.skirentalshop.security.registration;

import lombok.*;
import my.project.skirentalshop.validation.FieldsValueMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
@FieldsValueMatch(field = "password", fieldMatch = "password2", message = "{validation.application-user.password-not-matching}")
public class RegistrationRequest {

    @NotBlank(message = "{validation.application-user.invalid-name.not-blank}")
    @Size(max = 30, message = "{validation.application-user.invalid-name.size}")
    private String name;

    @Pattern(regexp = "(\\d\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2})", message = "{validation.client.invalid-phone-number}")
    private String phone1;

    @Pattern(regexp = "(\\d\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2})|(^$)", message = "{validation.client.invalid-phone-number}")
    private String phone2;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.application-user.invalid-email}")
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$", message = "{validation.application-user.password-invalid}")
    private String password;

    private String password2;
}
