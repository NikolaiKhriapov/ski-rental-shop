package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.Objects;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.*;

@Entity
public class RiderAssignedEquipment {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment snowboard;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment snowboardBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment ski;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment skiBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment helmet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment jacket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment gloves;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment pants;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment protectiveShorts;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Equipment kneeProtection;

    @OneToOne(mappedBy = "riderAssignedEquipment")
    private BookingRiderEquipmentLink bookingRiderEquipmentLink;

    public RiderAssignedEquipment() {
    }

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

    public void setSnowboard(Equipment snowboard) {
        this.snowboard = snowboard;
    }

    public Equipment getSnowboardBoots() {
        if (snowboardBoots == null) {
            Equipment snowboardBoots = new Equipment();
            snowboardBoots.setType(SNOWBOARD_BOOTS);
            return snowboardBoots;
        }
        return snowboardBoots;
    }

    public void setSnowboardBoots(Equipment snowboardBoots) {
        this.snowboardBoots = snowboardBoots;
    }

    public Equipment getSki() {
        if (ski == null) {
            Equipment ski = new Equipment();
            ski.setType(SKI);
            return ski;
        }
        return ski;
    }

    public void setSki(Equipment ski) {
        this.ski = ski;
    }

    public Equipment getSkiBoots() {
        if (skiBoots == null) {
            Equipment skiBoots = new Equipment();
            skiBoots.setType(SKI_BOOTS);
            return skiBoots;
        }
        return skiBoots;
    }

    public void setSkiBoots(Equipment skiBoots) {
        this.skiBoots = skiBoots;
    }

    public Equipment getHelmet() {
        if (helmet == null) {
            Equipment helmet = new Equipment();
            helmet.setType(HELMET);
            return helmet;
        }
        return helmet;
    }

    public void setHelmet(Equipment helmet) {
        this.helmet = helmet;
    }

    public Equipment getJacket() {
        if (jacket == null) {
            Equipment jacket = new Equipment();
            jacket.setType(JACKET);
            return jacket;
        }
        return jacket;
    }

    public void setJacket(Equipment jacket) {
        this.jacket = jacket;
    }

    public Equipment getGloves() {
        if (gloves == null) {
            Equipment gloves = new Equipment();
            gloves.setType(GLOVES);
            return gloves;
        }
        return gloves;
    }

    public void setGloves(Equipment gloves) {
        this.gloves = gloves;
    }

    public Equipment getPants() {
        if (pants == null) {
            Equipment pants = new Equipment();
            pants.setType(PANTS);
            return pants;
        }
        return pants;
    }

    public void setPants(Equipment pants) {
        this.pants = pants;
    }

    public Equipment getProtectiveShorts() {
        if (protectiveShorts == null) {
            Equipment protectiveShorts = new Equipment();
            protectiveShorts.setType(PROTECTIVE_SHORTS);
            return protectiveShorts;
        }
        return protectiveShorts;
    }

    public void setProtectiveShorts(Equipment protectiveShorts) {
        this.protectiveShorts = protectiveShorts;
    }

    public Equipment getKneeProtection() {
        if (kneeProtection == null) {
            Equipment kneeProtection = new Equipment();
            kneeProtection.setType(KNEE_PROTECTION);
            return kneeProtection;
        }
        return kneeProtection;
    }

    public void setKneeProtection(Equipment kneeProtection) {
        this.kneeProtection = kneeProtection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiderAssignedEquipment that = (RiderAssignedEquipment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(snowboard, that.snowboard) &&
                Objects.equals(snowboardBoots, that.snowboardBoots) &&
                Objects.equals(ski, that.ski) &&
                Objects.equals(skiBoots, that.skiBoots) &&
                Objects.equals(helmet, that.helmet) &&
                Objects.equals(jacket, that.jacket) &&
                Objects.equals(gloves, that.gloves) &&
                Objects.equals(pants, that.pants) &&
                Objects.equals(protectiveShorts, that.protectiveShorts) &&
                Objects.equals(kneeProtection, that.kneeProtection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, snowboard, snowboardBoots, ski, skiBoots,
                helmet, jacket, gloves, pants, protectiveShorts, kneeProtection);
    }

    @Override
    public String toString() {
        return "RiderAssignedEquipment{" +
                "id=" + id +
                ", snowboard=" + snowboard +
                ", snowboardBoots=" + snowboardBoots +
                ", ski=" + ski +
                ", skiBoots=" + skiBoots +
                ", helmet=" + helmet +
                ", jacket=" + jacket +
                ", gloves=" + gloves +
                ", pants=" + pants +
                ", protectiveShorts=" + protectiveShorts +
                ", kneeProtection=" + kneeProtection +
                '}';
    }
}
