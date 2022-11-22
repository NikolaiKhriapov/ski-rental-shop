package my.project.skirentalshop.entity;

import lombok.*;
import my.project.skirentalshop.entity.enums.*;
import my.project.skirentalshop.validation.EquipmentSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EquipmentSize
public class Equipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypesOfEquipment type;

    @NotBlank(message = "{validation.equipment.invalid-name.not-empty}")
    @Size(message = "{validation.equipment.invalid-name.size}")
    private String name;

    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    private String size;

    @Enumerated(EnumType.STRING)
    private Stiffness stiffness;

    @Enumerated(EnumType.STRING)
    private Arch arch;

    @Enumerated(EnumType.STRING)
    private BindingSize bindingSize;

    @ManyToMany(mappedBy = "riderAssignedEquipment")
    @ToString.Exclude
    private List<BookingRiderEquipmentLink> bookingRiderEquipmentLink;
}
