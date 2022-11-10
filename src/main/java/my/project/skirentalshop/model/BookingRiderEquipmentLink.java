package my.project.skirentalshop.model;

import lombok.*;
import my.project.skirentalshop.model.enums.TypesOfEquipment;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "booking_rider_equipment_link")
public class BookingRiderEquipmentLink {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rider_assigned_equipment_id")
    private RiderAssignedEquipment riderAssignedEquipment;

    @ElementCollection(targetClass = TypesOfEquipment.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "link_requested_equipment",
            joinColumns = @JoinColumn(name = "booking_rider_equipment_link_id"))
    private List<TypesOfEquipment> riderRequestedEquipment;

    public BookingRiderEquipmentLink(Booking booking,
                                     Rider rider,
                                     RiderAssignedEquipment riderAssignedEquipment,
                                     List<TypesOfEquipment> riderRequestedEquipment) {
        this.booking = booking;
        this.rider = rider;
        this.riderAssignedEquipment = riderAssignedEquipment;
        this.riderRequestedEquipment = riderRequestedEquipment;
    }
}
