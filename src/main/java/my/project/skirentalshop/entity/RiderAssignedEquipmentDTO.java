package my.project.skirentalshop.entity;

import lombok.*;

import javax.persistence.*;

import static my.project.skirentalshop.entity.enums.TypesOfEquipment.*;

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
