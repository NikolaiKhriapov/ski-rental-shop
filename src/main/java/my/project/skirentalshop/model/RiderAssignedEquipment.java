package my.project.skirentalshop.model;

import lombok.*;

import javax.persistence.*;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.*;

@Entity
@NoArgsConstructor
@Data
@Table(name = "rider_assigned_equipment")
public class RiderAssignedEquipment {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "snowboard_id")
    private Equipment snowboard;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "snowboard_boots_id")
    private Equipment snowboardBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ski_id")
    private Equipment ski;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ski_boots_id")
    private Equipment skiBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "helmet_id")
    private Equipment helmet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jacket_id")
    private Equipment jacket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gloves_id")
    private Equipment gloves;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pants_id")
    private Equipment pants;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "protective_shorts_id")
    private Equipment protectiveShorts;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "knee_protection_id")
    private Equipment kneeProtection;

    @OneToOne(mappedBy = "riderAssignedEquipment")
    @ToString.Exclude
    private BookingRiderEquipmentLink bookingRiderEquipmentLink;


    public Long getId() {
        return id;
    }

    public Equipment getSnowboard() {
        if (snowboard == null) {
            Equipment snowboard = new Equipment();
            snowboard.setType(SNOWBOARD);
            return snowboard;
        }
        return snowboard;
    }

    public Equipment getSnowboardBoots() {
        if (snowboardBoots == null) {
            Equipment snowboardBoots = new Equipment();
            snowboardBoots.setType(SNOWBOARD_BOOTS);
            return snowboardBoots;
        }
        return snowboardBoots;
    }

    public Equipment getSki() {
        if (ski == null) {
            Equipment ski = new Equipment();
            ski.setType(SKI);
            return ski;
        }
        return ski;
    }

    public Equipment getSkiBoots() {
        if (skiBoots == null) {
            Equipment skiBoots = new Equipment();
            skiBoots.setType(SKI_BOOTS);
            return skiBoots;
        }
        return skiBoots;
    }

    public Equipment getHelmet() {
        if (helmet == null) {
            Equipment helmet = new Equipment();
            helmet.setType(HELMET);
            return helmet;
        }
        return helmet;
    }

    public Equipment getJacket() {
        if (jacket == null) {
            Equipment jacket = new Equipment();
            jacket.setType(JACKET);
            return jacket;
        }
        return jacket;
    }

    public Equipment getGloves() {
        if (gloves == null) {
            Equipment gloves = new Equipment();
            gloves.setType(GLOVES);
            return gloves;
        }
        return gloves;
    }

    public Equipment getPants() {
        if (pants == null) {
            Equipment pants = new Equipment();
            pants.setType(PANTS);
            return pants;
        }
        return pants;
    }

    public Equipment getProtectiveShorts() {
        if (protectiveShorts == null) {
            Equipment protectiveShorts = new Equipment();
            protectiveShorts.setType(PROTECTIVE_SHORTS);
            return protectiveShorts;
        }
        return protectiveShorts;
    }

    public Equipment getKneeProtection() {
        if (kneeProtection == null) {
            Equipment kneeProtection = new Equipment();
            kneeProtection.setType(KNEE_PROTECTION);
            return kneeProtection;
        }
        return kneeProtection;
    }
}
