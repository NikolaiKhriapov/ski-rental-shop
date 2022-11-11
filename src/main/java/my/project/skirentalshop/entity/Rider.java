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
@Table(name = "rider")
public class Rider {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "{validation.rider.name}")
    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull(message = "{validation.rider.height}")
    @DecimalMin(value = "60", message = "{validation.rider.height}")
    @DecimalMax(value = "220", message = "{validation.rider.height}")
    @Column(name = "height")
    private Double height;

    @NotNull(message = "{validation.rider.weight}")
    @DecimalMin(value = "50", message = "{validation.rider.weight}")
    @DecimalMax(value = "160", message = "{validation.rider.weight}")
    @Column(name = "weight")
    private Double weight;

    @Column(name = "foot_size")
    @Enumerated(EnumType.STRING)
    private FootSize footSize;

    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;

    public List<BookingRiderEquipmentLink> getListOfBookingRiderEquipmentLinks() {
        if (listOfBookingRiderEquipmentLinks == null) {
            listOfBookingRiderEquipmentLinks = new ArrayList<>();
        }
        return listOfBookingRiderEquipmentLinks;
    }
}
