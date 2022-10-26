package my.project.skirentalshop.model.enums;

import java.util.ResourceBundle;

public enum Stiffness {
    UNKNOWN,
    SOFT,
    MEDIUM,
    HARD;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    public String toString() {
        return resourceBundle.getString("snowboard.stiffness." + name());
    }
}
