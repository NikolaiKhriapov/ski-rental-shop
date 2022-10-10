package my.project.skirentalshop.model;

import java.util.ResourceBundle;

public enum TypesOfEquipment {
    SNOWBOARD,
    SKI,
    SNOWBOARD_BOOTS,
    SKI_BOOTS,
    JACKET,
    PANTS,
    KNEE_PROTECTION,
    PROTECTIVE_SHORTS,
    HELMET,
    GLOVES;

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    @Override
    public String toString() {
        return resourceBundle.getString("equipment.type." + name());
    }

}