package my.project.skirentalshop.model;

import lombok.*;

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

    enum FootSize {
        OTHER,
        RU36_EU37_MM235,
        RU34_EU35_MM240,
        RU37_EU38_MM245,
        RU38_EU39_MM250,
        RU39_EU40_MM255,
        RU40_EU41_MM260,
        RU41_EU42_MM265,
        RU415_EU425_MM270,
        RU42_EU43_MM275,
        RU425_EU435_MM280,
        RU43_EU44_MM285,
        RU44_EU45_MM290,
        RU45_EU46_MM300,
        RU46_EU47_MM310,
        JUNIOR_26_MM165,
        JUNIOR_28_MM175,
        JUNIOR_30_MM185,
        JUNIOR_31_MM195,
        JUNIOR_32_MM205,
        JUNIOR_33_MM210,
        JUNIOR_34_MM215,
        JUNIOR_35_MM225,
        JUNIOR_36_MM235,
        JUNIOR_37_MM245,
        JUNIOR_39_MM255;

        private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("rider");

        @Override
        public String toString() {
            return resourceBundle.getString("riders.foot-size." + name());
        }
    }

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

    @OneToMany(mappedBy = "rider")
    private List<BookingRiderEquipmentLink> listOfBookingRiderEquipmentLinks;
}
