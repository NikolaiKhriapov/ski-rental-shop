package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class RiderAssignedEquipment {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Snowboard snowboard;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private SnowboardBoots snowboardBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Ski ski;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private SkiBoots skiBoots;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Helmet helmet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Jacket jacket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Gloves gloves;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Pants pants;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ProtectiveShorts protectiveShorts;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private KneeProtection kneeProtection;

    @OneToOne(mappedBy = "riderAssignedEquipment")
    private BookingRiderEquipmentLink bookingRiderEquipmentLink;

    public RiderAssignedEquipment() {
    }

    public Long getId() {
        return id;
    }

    public Snowboard getSnowboard() {
        if (snowboard == null) {
            return new Snowboard();
        }
        return snowboard;
    }

    public void setSnowboard(Snowboard snowboard) {
        this.snowboard = snowboard;
    }

    public SnowboardBoots getSnowboardBoots() {
        if (snowboardBoots == null) {
            return new SnowboardBoots();
        }
        return snowboardBoots;
    }

    public void setSnowboardBoots(SnowboardBoots snowboardBoots) {
        this.snowboardBoots = snowboardBoots;
    }

    public Ski getSki() {
        if (ski == null) {
            return new Ski();
        }
        return ski;
    }

    public void setSki(Ski ski) {
        this.ski = ski;
    }

    public SkiBoots getSkiBoots() {
        if (skiBoots == null) {
            return new SkiBoots();
        }
        return skiBoots;
    }

    public void setSkiBoots(SkiBoots skiBoots) {
        this.skiBoots = skiBoots;
    }

    public Helmet getHelmet() {
        if (helmet == null) {
            return new Helmet();
        }
        return helmet;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }

    public Jacket getJacket() {
        if (jacket == null) {
            return new Jacket();
        }
        return jacket;
    }

    public void setJacket(Jacket jacket) {
        this.jacket = jacket;
    }

    public Gloves getGloves() {
        if (gloves == null) {
            return new Gloves();
        }
        return gloves;
    }

    public void setGloves(Gloves gloves) {
        this.gloves = gloves;
    }

    public Pants getPants() {
        if (pants == null) {
            return new Pants();
        }
        return pants;
    }

    public void setPants(Pants pants) {
        this.pants = pants;
    }

    public ProtectiveShorts getProtectiveShorts() {
        if (protectiveShorts == null) {
            return new ProtectiveShorts();
        }
        return protectiveShorts;
    }

    public void setProtectiveShorts(ProtectiveShorts protectiveShorts) {
        this.protectiveShorts = protectiveShorts;
    }

    public KneeProtection getKneeProtection() {
        if (kneeProtection == null) {
            return new KneeProtection();
        }
        return kneeProtection;
    }

    public void setKneeProtection(KneeProtection kneeProtection) {
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
