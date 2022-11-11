package my.project.skirentalshop.entity.enums;

import java.util.ResourceBundle;

public enum Stiffness {
    UNKNOWN,
    SOFT,
    MEDIUM,
    HARD;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    public String toString() {
        return resourceBundle.getString("equipment.stiffness." + name());
    }
}
