package my.project.skirentalshop.model.enums;

import java.util.ResourceBundle;

public enum TypesOfEquipment {
    SNOWBOARD("snowboard", "snowboards"),
    SNOWBOARD_BOOTS("snowboard-boots", "snowboard-boots"),
    SKI("ski", "ski"),
    SKI_BOOTS("ski-boots", "ski-boots"),
    HELMET("helmet", "helmets"),
    JACKET("jacket", "jackets"),
    GLOVES("gloves", "gloves"),
    PANTS("pants", "pants"),
    PROTECTIVE_SHORTS("protective-shorts", "protective-shorts"),
    KNEE_PROTECTION("knee-protection", "knee-protection");

    public final String nameSingle;
    public final String namePlural;
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    TypesOfEquipment(String nameSingle, String namePlural) {
        this.nameSingle = nameSingle;
        this.namePlural = namePlural;
    }

    @Override
    public String toString() {
        return resourceBundle.getString("equipment.type." + name());
    }

    public static TypesOfEquipment convertToEnumField(String inputType) {
        for (TypesOfEquipment oneType : TypesOfEquipment.values()) {
            if (oneType.nameSingle.equalsIgnoreCase(inputType) || oneType.namePlural.equalsIgnoreCase(inputType)) {
                return oneType;
            }
        }
        return null;
    }
}
