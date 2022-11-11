package my.project.skirentalshop.validation;

import my.project.skirentalshop.entity.Equipment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EquipmentSizeValidator implements ConstraintValidator<EquipmentSize, Equipment> {

    public boolean isValid(Equipment equipment, ConstraintValidatorContext context) {
        switch (equipment.getType()) {
            case SNOWBOARD -> {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate("{validation.snowboard.invalid-size}")
                        .addConstraintViolation();
                return equipment.getSize() != null && equipment.getSize().matches("(1[0-6][0-9]|170)([w|W]?)");
            }
            case SKI -> {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate("{validation.ski.invalid-size}")
                        .addConstraintViolation();
                return equipment.getSize() != null && equipment.getSize().matches("(1[0-6][0-9]|170)");
            }
            default -> {
                return true;
            }
        }
    }
}
