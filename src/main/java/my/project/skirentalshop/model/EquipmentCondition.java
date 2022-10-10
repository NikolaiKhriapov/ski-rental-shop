package my.project.skirentalshop.model;

import java.util.ResourceBundle;

public enum EquipmentCondition {
    UNKNOWN,
    GOOD,
    USABLE,
    BROKEN,
    SERVICE;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    public String toString() {
        return resourceBundle.getString("equipment_condition." + name());
    }
}
