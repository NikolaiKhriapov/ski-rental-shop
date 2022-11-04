package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.FootSize;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Rider {

    enum Sex {
        MALE,
        FEMALE;

        private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("rider");

        @Override
        public String toString() {
            return resourceBundle.getString("riders.sex." + name());
        }
    }

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotEmpty(message = "{validation.rider.name}")
    private String name;

    private Sex sex;

    @NotNull(message = "{validation.rider.height}")
    @DecimalMin(value = "60", message = "{validation.rider.height}")
    @DecimalMax(value = "220", message = "{validation.rider.height}")
    private Double height; //TODO: handle java.lang.NumberFormatException

    @NotNull(message = "{validation.rider.weight}")
    @DecimalMin(value = "50", message = "{validation.rider.weight}")
    @DecimalMax(value = "160", message = "{validation.rider.weight}")
    private Double weight; //TODO: handle java.lang.NumberFormatException

    private FootSize footSize;

    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;

    public List<BookingRiderEquipmentLink> getListOfBookingRiderEquipmentLinks() {
        if (listOfBookingRiderEquipmentLinks == null) {
            listOfBookingRiderEquipmentLinks = new ArrayList<>();
        }
        return listOfBookingRiderEquipmentLinks;
    }
}
