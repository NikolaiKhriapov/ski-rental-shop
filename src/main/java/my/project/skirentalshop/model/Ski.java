package my.project.skirentalshop.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Entity
public class Ski {

    enum Stiffness {
        UNKNOWN,
        SOFT,
        MEDIUM,
        HARD;

        private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("equipment");

        public String toString() {
            return resourceBundle.getString("ski.stiffness." + name());
        }
    }

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @javax.validation.constraints.Size(min = 3, max = 30, message = "{validation.ski.invalid_name}")
    private String name;
    private EquipmentCondition condition;
    private String size;
    private Stiffness stiffness;
    @OneToMany(mappedBy = "ski")
    private List<AssignedEquipment> listOfAssignedEquipment;

    public Ski() {
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Stiffness getStiffness() {
        return stiffness;
    }

    public void setStiffness(Stiffness stiffness) {
        this.stiffness = stiffness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ski ski = (Ski) o;
        return Objects.equals(id, ski.id) && Objects.equals(name, ski.name) && condition == ski.condition &&
                Objects.equals(size, ski.size) && stiffness == ski.stiffness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, size, stiffness);
    }

    @Override
    public String toString() {
        return "Ski{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition.name() +
                ", size='" + size +
                ", stiffness=" + stiffness.name() +
                '}';
    }
}
