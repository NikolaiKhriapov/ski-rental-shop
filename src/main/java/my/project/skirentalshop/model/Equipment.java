package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
public class Equipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    Long id;

    TypesOfEquipment type;

    @NotBlank(message = "{validation.equipment.invalid-name.not-empty}")
    @Size(message = "{validation.equipment.invalid-name.size}")
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

    //TODO: find better solution
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
