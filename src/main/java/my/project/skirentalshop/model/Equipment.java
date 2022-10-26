package my.project.skirentalshop.model;

import my.project.skirentalshop.model.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Equipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    Long id;

    TypesOfEquipment type;

    @NotBlank(message = "{validation.equipment.invalid_name.not_empty}")
    @javax.validation.constraints.Size(message = "{validation.equipment.invalid_name.clothesSize}")
    String name;

    EquipmentCondition condition;

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
}
