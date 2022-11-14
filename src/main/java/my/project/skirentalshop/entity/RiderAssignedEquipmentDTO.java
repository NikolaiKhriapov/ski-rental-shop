package my.project.skirentalshop.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Data
public class RiderAssignedEquipmentDTO {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment snowboard;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment snowboardBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment ski;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment skiBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment helmet;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment jacket;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment gloves;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment pants;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment protectiveShorts;

    @ManyToOne(cascade = CascadeType.ALL)
    private Equipment kneeProtection;

    @OneToOne(mappedBy = "riderAssignedEquipment")
    @ToString.Exclude
    private BookingRiderEquipmentLink bookingRiderEquipmentLink;
}
