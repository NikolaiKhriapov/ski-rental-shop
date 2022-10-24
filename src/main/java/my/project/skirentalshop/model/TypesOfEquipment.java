package my.project.skirentalshop.model;

import java.util.ResourceBundle;

public enum TypesOfEquipment {
    SNOWBOARD,
    SKI,
    SNOWBOARD_BOOTS,
    SKI_BOOTS,
    HELMET,
    JACKET,
    GLOVES,
    PANTS,
    PROTECTIVE_SHORTS,
    KNEE_PROTECTION;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    @Override
    public String toString() {
        return resourceBundle.getString("equipment.type." + name());
    }
}