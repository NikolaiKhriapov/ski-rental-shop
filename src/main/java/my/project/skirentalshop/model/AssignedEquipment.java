package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class AssignedEquipment {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @OneToOne(mappedBy = "assignedEquipment")
    private Rider rider;

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
    private Jacket jacket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private KneeProtection kneeProtection;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ProtectiveShorts protectiveShorts;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Helmet helmet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Pants pants;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Gloves gloves;

    public AssignedEquipment() {
    }

    public Long getId() {
        return id;
    }

    public Rider getRider() {
        if (rider == null) {
            return new Rider();
        }
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
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

    public Jacket getJacket() {
        if (jacket == null) {
            return new Jacket();
        }
        return jacket;
    }

    public void setJacket(Jacket jacket) {
        this.jacket = jacket;
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

    public ProtectiveShorts getProtectiveShorts() {
        if (protectiveShorts == null) {
            return new ProtectiveShorts();
        }
        return protectiveShorts;
    }

    public void setProtectiveShorts(ProtectiveShorts protectiveShorts) {
        this.protectiveShorts = protectiveShorts;
    }

    public Helmet getHelmet() {
        if(helmet == null){
            return new Helmet();
        }
        return helmet;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }

    public Pants getPants() {
        if(pants == null){
            return new Pants();
        }
        return pants;
    }

    public void setPants(Pants pants) {
        this.pants = pants;
    }

    public Gloves getGloves() {
        if(gloves == null){
            return new Gloves();
        }
        return gloves;
    }

    public void setGloves(Gloves gloves) {
        this.gloves = gloves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignedEquipment that = (AssignedEquipment) o;
        return Objects.equals(id, that.id) && Objects.equals(rider, that.rider) && Objects.equals(snowboard, that.snowboard) &&
                Objects.equals(snowboardBoots, that.snowboardBoots) && Objects.equals(ski, that.ski) &&
                Objects.equals(skiBoots, that.skiBoots) && Objects.equals(jacket, that.jacket) &&
                Objects.equals(kneeProtection, that.kneeProtection) && Objects.equals(protectiveShorts, that.protectiveShorts) &&
                Objects.equals(helmet, that.helmet) && Objects.equals(pants, that.pants) && Objects.equals(gloves, that.gloves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rider, snowboard, snowboardBoots, ski, skiBoots, jacket, kneeProtection,
                protectiveShorts, helmet, pants, gloves);
    }

    @Override
    public String toString() {
        return "AssignedEquipment{" +
                "id=" + id +
                ", rider=" + rider.toString() +
                ", snowboard=" + snowboard.toString() +
                ", snowboardBoots=" + snowboardBoots.toString() +
                ", ski=" + ski.toString() +
                ", skiBoots=" + skiBoots.toString() +
                ", jacket=" + jacket.toString() +
                ", kneeProtection=" + kneeProtection.toString() +
                ", protectiveShorts=" + protectiveShorts.toString() +
                ", helmet=" + helmet.toString() +
                ", pants=" + pants.toString() +
                ", gloves=" + gloves.toString() +
                '}';
    }
}
