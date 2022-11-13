package my.project.skirentalshop.entity;

import lombok.*;
import my.project.skirentalshop.entity.enums.TypesOfEquipment;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class BookingRiderEquipmentLink {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Booking booking;

    @ManyToOne(cascade = CascadeType.ALL)
    private Rider rider;

    @ElementCollection(targetClass = TypesOfEquipment.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "link_to_requested_equipment")
    @Enumerated(EnumType.STRING)
    private List<TypesOfEquipment> riderRequestedEquipment;

    @ManyToMany
    @JoinTable(name = "link_to_assigned_equipment")
    private List<Equipment> riderAssignedEquipment;

    public BookingRiderEquipmentLink(Booking booking,
                                     Rider rider,
                                     List<Equipment> riderAssignedEquipment,
                                     List<TypesOfEquipment> riderRequestedEquipment) {
        this.booking = booking;
        this.rider = rider;
        this.riderAssignedEquipment = riderAssignedEquipment;
        this.riderRequestedEquipment = riderRequestedEquipment;
    }
}
