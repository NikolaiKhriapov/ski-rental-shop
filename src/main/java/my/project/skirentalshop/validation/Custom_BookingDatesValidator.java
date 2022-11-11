package my.project.skirentalshop.validation;

import my.project.skirentalshop.entity.Booking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Custom_BookingDatesValidator implements ConstraintValidator<Custom_BookingDates, Booking> {

    public boolean isValid(Booking booking, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate("{validation.application-user.invalid-dates}")
                .addConstraintViolation();
        return booking.getDateOfArrival() != null &&
                booking.getDateOfReturn() != null &&
                booking.getDateOfArrival().before(booking.getDateOfReturn());
    }
}
