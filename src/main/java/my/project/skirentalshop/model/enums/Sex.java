package my.project.skirentalshop.model.enums;

import java.util.ResourceBundle;

public enum Sex {
    MALE,
    FEMALE;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("rider");

    @Override
    public String toString() {
        return resourceBundle.getString("riders.sex." + name());
    }
}
