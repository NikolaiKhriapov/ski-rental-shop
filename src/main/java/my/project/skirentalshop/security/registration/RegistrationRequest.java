package my.project.skirentalshop.security.registration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegistrationRequest {
    @NotBlank(message = "{validation.application_user.invalid_name.not_blank}")
    @Size(max = 30, message = "{validation.application_user.invalid_name.size}")
    private String name;
    @NotBlank(message = "{validation.application_user.invalid_surname.not_blank}")
    @Size(max = 30, message = "{validation.application_user.invalid_surname.size}")
    private String surname;
    @Pattern(regexp = "[\\d]\\([\\d]{3}\\)[\\d]{3}-[\\d]{2}-[\\d]{2}", message = "{validation.application_user.invalid_phone_number}")
    private String phone1;
    @Pattern(regexp = "[\\d]\\([\\d]{3}\\)[\\d]{3}-[\\d]{2}-[\\d]{2}", message = "{validation.application_user.invalid_phone_number}")
    private String phone2;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.application_user.invalid_email}")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$", message = "{validation.application_user.password_invalid}")
    private String password;

    public RegistrationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationRequest that = (RegistrationRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(phone1, that.phone1) && Objects.equals(phone2, that.phone2) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone1, phone2, email, password);
    }

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "name='" + name +
                ", surname='" + surname +
                ", phone1='" + phone1 +
                ", phone2='" + phone2 +
                ", email='" + email +
                ", password='" + password +
                '}';
    }
}
