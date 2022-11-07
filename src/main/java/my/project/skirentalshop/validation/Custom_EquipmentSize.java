package my.project.skirentalshop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Custom_EquipmentSizeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Custom_EquipmentSize {

    String message() default "Invalid size format!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
