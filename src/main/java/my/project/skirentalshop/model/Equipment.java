package my.project.skirentalshop.model;

import my.project.skirentalshop.model.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
public class Equipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    Long id;
    TypesOfEquipment type;
    @NotBlank(message = "{validation.equipment.invalid-name.not-empty}")
    @javax.validation.constraints.Size(message = "{validation.equipment.invalid-name.size}")
    String name;
    EquipmentCondition condition;
    @Pattern(regexp = "(1[0-6][0-9]|170)([w|W]?)", message = "{validation.snowboard.invalid-size}")
    String snowboardSize; //Snowboard
    String skiSize; //Ski
    BootsSize bootsSize; //SnowboardBoots, SkiBoots
    ClothesSize clothesSize; //Helmet, Jacket, Gloves, Pants, ProtectiveShorts, KneeProtection
    Stiffness stiffness; //Snowboard, SnowboardBoots, Ski, SkiBoots
    Arch arch; //Snowboard
    BindingSize bindingSize; //Snowboard

    public Long getId() {
        return id;
    }

    public TypesOfEquipment getType() {
        return type;
    }

    public void setType(TypesOfEquipment type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentCondition getCondition() {
        return condition;
    }

    public void setCondition(EquipmentCondition condition) {
        this.condition = condition;
    }

    public String getSnowboardSize() {
        return snowboardSize;
    }

    public void setSnowboardSize(String snowboardSize) {
        this.snowboardSize = snowboardSize;
    }

    public String getSkiSize() {
        return skiSize;
    }

    public void setSkiSize(String skiSize) {
        this.skiSize = skiSize;
    }

    public BootsSize getBootsSize() {
        return bootsSize;
    }

    public void setBootsSize(BootsSize bootsSize) {
        this.bootsSize = bootsSize;
    }

    public ClothesSize getClothesSize() {
        return clothesSize;
    }

    public void setClothesSize(ClothesSize clothesSize) {
        this.clothesSize = clothesSize;
    }

    public Stiffness getStiffness() {
        return stiffness;
    }

    public void setStiffness(Stiffness stiffness) {
        this.stiffness = stiffness;
    }

    public Arch getArch() {
        return arch;
    }

    public void setArch(Arch arch) {
        this.arch = arch;
    }

    public BindingSize getBindingSize() {
        return bindingSize;
    }

    public void setBindingSize(BindingSize bindingSize) {
        this.bindingSize = bindingSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return switch (type) {
            case SNOWBOARD ->
                    Objects.equals(id, equipment.id) &&
                            type == equipment.type &&
                            Objects.equals(name, equipment.name) &&
                            condition == equipment.condition &&
                            Objects.equals(snowboardSize, equipment.snowboardSize) &&
                            stiffness == equipment.stiffness &&
                            arch == equipment.arch &&
                            bindingSize == equipment.bindingSize;
            case SKI ->
                    Objects.equals(id, equipment.id) &&
                            type == equipment.type &&
                            Objects.equals(name, equipment.name) &&
                            condition == equipment.condition &&
                            Objects.equals(skiSize, equipment.skiSize) &&
                            stiffness == equipment.stiffness;
            case SNOWBOARD_BOOTS, SKI_BOOTS ->
                    Objects.equals(id, equipment.id) &&
                            type == equipment.type &&
                            Objects.equals(name, equipment.name) &&
                            condition == equipment.condition &&
                            bootsSize == equipment.bootsSize &&
                            stiffness == equipment.stiffness;
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION ->
                    Objects.equals(id, equipment.id) &&
                            type == equipment.type &&
                            Objects.equals(name, equipment.name) &&
                            condition == equipment.condition &&
                            clothesSize == equipment.clothesSize;
        };
    }

    @Override
    public int hashCode() {
        return switch (type) {
            case SNOWBOARD ->
                    Objects.hash(id, type, name, condition, snowboardSize, stiffness, arch, bindingSize);
            case SKI ->
                    Objects.hash(id, type, name, condition, skiSize, stiffness);
            case SNOWBOARD_BOOTS, SKI_BOOTS ->
                    Objects.hash(id, type, name, condition, bootsSize, stiffness);
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION ->
                    Objects.hash(id, type, name, condition, clothesSize);
        };
    }

    @Override
    public String toString() {
        return switch (type) {
            case SNOWBOARD ->
                    "Equipment{" +
                            "id=" + id +
                            "type=" + type +
                            ", name='" + name +
                            ", condition=" + condition +
                            ", snowboardSize='" + snowboardSize +
                            ", stiffness=" + stiffness +
                            ", arch=" + arch +
                            ", bindingSize=" + bindingSize +
                            '}';
            case SKI ->
                    "Equipment{" +
                            "id=" + id +
                            "type=" + type +
                            ", name='" + name +
                            ", condition=" + condition +
                            ", skiSize='" + skiSize +
                            ", stiffness=" + stiffness +
                            '}';
            case SNOWBOARD_BOOTS, SKI_BOOTS ->
                    "Equipment{" +
                            "id=" + id +
                            "type=" + type +
                            ", name='" + name +
                            ", condition=" + condition +
                            ", bootsSize=" + bootsSize +
                            ", stiffness=" + stiffness +
                            '}';
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION ->
                    "Equipment{" +
                            "id=" + id +
                            "type=" + type +
                            ", name='" + name +
                            ", condition=" + condition +
                            ", clothesSize=" + clothesSize +
                            '}';
        };
    }
}
