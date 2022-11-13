package my.project.skirentalshop.entity;

import lombok.*;
import my.project.skirentalshop.entity.enums.FootSize;
import my.project.skirentalshop.entity.enums.Sex;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Data
public class Rider {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotEmpty(message = "{validation.rider.name}")
    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull(message = "{validation.rider.height}")
    @DecimalMin(value = "60", message = "{validation.rider.height}")
    @DecimalMax(value = "220", message = "{validation.rider.height}")
    private Double height;

    @NotNull(message = "{validation.rider.weight}")
    @DecimalMin(value = "50", message = "{validation.rider.weight}")
    @DecimalMax(value = "160", message = "{validation.rider.weight}")
    private Double weight;

    @Enumerated(EnumType.STRING)
    private FootSize footSize;

    @OneToMany(mappedBy = "rider", cascade = CascadeType.MERGE)
    @ToString.Exclude
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;

    public List<BookingRiderEquipmentLink> getListOfBookingRiderEquipmentLinks() {
        if (listOfBookingRiderEquipmentLinks == null) {
            listOfBookingRiderEquipmentLinks = new ArrayList<>();
        }
        return listOfBookingRiderEquipmentLinks;
    }
}
