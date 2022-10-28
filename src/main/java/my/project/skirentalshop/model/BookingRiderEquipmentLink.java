package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.TypesOfEquipment;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingRiderEquipmentLink {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Booking booking;

    @ManyToOne
    @JoinColumn
    private Rider rider;

    @ElementCollection(targetClass = TypesOfEquipment.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn)
    private List<TypesOfEquipment> riderRequestedEquipment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private RiderAssignedEquipment riderAssignedEquipment;

    public BookingRiderEquipmentLink(Booking booking,
                                     Rider rider,
                                     List<TypesOfEquipment> riderRequestedEquipment,
                                     RiderAssignedEquipment riderAssignedEquipment) {
        this.booking = booking;
        this.rider = rider;
        this.riderRequestedEquipment = riderRequestedEquipment;
        this.riderAssignedEquipment = riderAssignedEquipment;
    }
}
