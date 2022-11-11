package my.project.skirentalshop.entity.enums;

import java.util.ResourceBundle;

public enum EquipmentCondition {
    UNKNOWN,
    GOOD,
    USABLE,
    BROKEN,
    SERVICE;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    public String toString() {
        return resourceBundle.getString("equipment.condition." + name());
    }
}
