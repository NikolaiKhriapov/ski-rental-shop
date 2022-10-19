package my.project.skirentalshop.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Entity
public class SkiBoots {

    public enum Stiffness {
        UNKNOWN(),
        SOFT(),
        MEDIUM(),
        HARD(),
        s40(),
        s50(),
        s60(),
        s70(),
        s80(),
        s90(),
        s100();

        private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

        public String toString() {
            return resourceBundle.getString("ski_boots.stiffness." + name());
        }
    }

    public enum Size {
        OTHER,
        RU36_EU37_MM235,
        RU37_EU38_MM245,
        RU39_EU40_MM255,
        RU40_EU41_MM260,
        RU41_EU42_MM265,
        RU42_EU43_MM275,
        RU43_EU44_MM285,
        RU44_EU45_MM295,
        RU45_EU46_MM305,
        RU46_EU47_MM315,
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

        private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

        public String toString() {
            return resourceBundle.getString("ski_boots.size." + name());
        }
    }

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @NotBlank(message = "{validation.equipment.invalid_name.not_empty}")
    @javax.validation.constraints.Size(message = "{validation.equipment.invalid_name.size}")
    private String name;
    private EquipmentCondition condition;
    private Size size;
    private Stiffness stiffness;
    @OneToMany(mappedBy = "skiBoots")
    private List<AssignedEquipment> listOfAssignedEquipment;

    public SkiBoots() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentCondition getCondition() {
        return condition;
    }

    public void setCondition(EquipmentCondition condition) {
        this.condition = condition;
    }

    public SkiBoots.Size getSize() {
        return size;
    }

    public void setSize(SkiBoots.Size size) {
        this.size = size;
    }

    public SkiBoots.Stiffness getStiffness() {
        return stiffness;
    }

    public void setStiffness(SkiBoots.Stiffness stiffness) {
        this.stiffness = stiffness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkiBoots skiBoots = (SkiBoots) o;
        return Objects.equals(id, skiBoots.id) && Objects.equals(name, skiBoots.name) &&
                condition == skiBoots.condition && size == skiBoots.size && stiffness == skiBoots.stiffness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, size, stiffness);
    }

    @Override
    public String toString() {
        return "SkiBoots{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition.name() +
                ", size=" + size.name() +
                ", stiffness=" + stiffness.name() +
                '}';
    }
}
