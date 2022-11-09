package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.*;
import my.project.skirentalshop.validation.Custom_EquipmentSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Data
@Custom_EquipmentSize
@Table(name = "equipment")
public class Equipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @Column(name = "id")
    Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    TypesOfEquipment type;

    @NotBlank(message = "{validation.equipment.invalid-name.not-empty}")
    @Size(message = "{validation.equipment.invalid-name.size}")
    @Column(name = "name")
    String name;

    @Column(name = "condition")
    @Enumerated(EnumType.STRING)
    EquipmentCondition condition;

    @Column(name = "size")
    String size;

    @Column(name = "stiffness")
    @Enumerated(EnumType.STRING)
    Stiffness stiffness; //Snowboard, SnowboardBoots, Ski, SkiBoots

    @Column(name = "arch")
    @Enumerated(EnumType.STRING)
    Arch arch; //Snowboard

    @Column(name = "binding_size")
    @Enumerated(EnumType.STRING)
    BindingSize bindingSize; //Snowboard
}
