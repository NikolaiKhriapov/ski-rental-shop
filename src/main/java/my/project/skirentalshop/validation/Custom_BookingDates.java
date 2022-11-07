package my.project.skirentalshop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Custom_BookingDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Custom_BookingDates {

    String message() default "Fields values don't match!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}