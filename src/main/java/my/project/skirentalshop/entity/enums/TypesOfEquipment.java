package my.project.skirentalshop.entity.enums;

import java.util.ResourceBundle;

public enum TypesOfEquipment {
    SNOWBOARD("snowboard", "snowboards", "snowboard"),
    SNOWBOARD_BOOTS("snowboard-boots", "snowboard-boots", "snowboardBoots"),
    SKI("ski", "ski", "ski"),
    SKI_BOOTS("ski-boots", "ski-boots", "skiBoots"),
    HELMET("helmet", "helmets", "helmet"),
    JACKET("jacket", "jackets", "jacket"),
    GLOVES("gloves", "gloves", "gloves"),
    PANTS("pants", "pants", "pants"),
    PROTECTIVE_SHORTS("protective-shorts", "protective-shorts", "protectiveShorts"),
    KNEE_PROTECTION("knee-protection", "knee-protection", "kneeProtection");

    public final String nameSingularDash;
    public final String namePluralDash;
    public final String nameSingularCamel;
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

    TypesOfEquipment(String nameSingularDash, String namePluralDash, String nameSingularCamel) {
        this.nameSingularDash = nameSingularDash;
        this.namePluralDash = namePluralDash;
        this.nameSingularCamel = nameSingularCamel;
    }

    @Override
    public String toString() {
        return resourceBundle.getString("equipment.type." + name());
    }

    public static TypesOfEquipment convertToEnumField(String inputType) {
        for (TypesOfEquipment oneType : TypesOfEquipment.values()) {
            if (oneType.nameSingularDash.equalsIgnoreCase(inputType) ||
                    oneType.namePluralDash.equalsIgnoreCase(inputType) ||
                    oneType.nameSingularCamel.equalsIgnoreCase(inputType)) {
                return oneType;
            }
        }
        return null;
    }
}
