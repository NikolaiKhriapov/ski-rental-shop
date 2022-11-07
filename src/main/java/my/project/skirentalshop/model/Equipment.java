package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.*;
import my.project.skirentalshop.validation.EquipmentSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
@EquipmentSize
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

    String size;

    Stiffness stiffness; //Snowboard, SnowboardBoots, Ski, SkiBoots

    Arch arch; //Snowboard

    BindingSize bindingSize; //Snowboard

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return switch (type) {
            case SNOWBOARD -> Objects.equals(id, equipment.id) &&
                    type == equipment.type &&
                    Objects.equals(name, equipment.name) &&
                    condition == equipment.condition &&
                    Objects.equals(size, equipment.size) &&
                    stiffness == equipment.stiffness &&
                    arch == equipment.arch &&
                    bindingSize == equipment.bindingSize;
            case SKI, SNOWBOARD_BOOTS, SKI_BOOTS -> Objects.equals(id, equipment.id) &&
                    type == equipment.type &&
                    Objects.equals(name, equipment.name) &&
                    condition == equipment.condition &&
                    Objects.equals(size, equipment.size) &&
                    stiffness == equipment.stiffness;
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION ->
                    Objects.equals(id, equipment.id) &&
                            type == equipment.type &&
                            Objects.equals(name, equipment.name) &&
                            condition == equipment.condition &&
                            Objects.equals(size, equipment.size);
        };
    }

    @Override
    public int hashCode() {
        return switch (type) {
            case SNOWBOARD -> Objects.hash(id, type, name, condition, size, stiffness, arch, bindingSize);
            case SKI, SNOWBOARD_BOOTS, SKI_BOOTS -> Objects.hash(id, type, name, condition, size, stiffness);
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION ->
                    Objects.hash(id, type, name, condition, size);
        };
    }

    @Override
    public String toString() { //TODO: enum fields do not get printed properly
        return switch (type) {
            case SNOWBOARD -> "Equipment{" +
                    "id=" + id +
                    "type=" + type +
                    ", name='" + name +
                    ", condition=" + condition +
                    ", size='" + size +
                    ", stiffness=" + stiffness +
                    ", arch=" + arch +
                    ", bindingSize=" + bindingSize +
                    '}';
            case SKI, SNOWBOARD_BOOTS, SKI_BOOTS -> "Equipment{" +
                    "id=" + id +
                    "type=" + type +
                    ", name='" + name +
                    ", condition=" + condition +
                    ", size=" + size +
                    ", stiffness=" + stiffness +
                    '}';
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION -> "Equipment{" +
                    "id=" + id +
                    "type=" + type +
                    ", name='" + name +
                    ", condition=" + condition +
                    ", size=" + size +
                    '}';
        };
    }
}
