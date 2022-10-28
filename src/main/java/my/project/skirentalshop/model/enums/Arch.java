package my.project.skirentalshop.model.enums;

import java.util.ResourceBundle;

public enum Arch {
    UNKNOWN,
    CAMBER,
    FLAT,
    ROCKER;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    @Override
    public String toString() {
        return resourceBundle.getString("equipment.arch." + name());
    }
}
