package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class KneeProtection {

    enum Size {
        XS,
        S,
        M,
        L,
        XL
    }

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @javax.validation.constraints.Size(min = 3, max = 30, message = "{validation.knee_protection.invalid_name}")
    private String name;
    private EquipmentCondition condition;
    private Size size;
    @OneToMany(mappedBy = "kneeProtection")
    private List<AssignedEquipment> listOfAssignedEquipment;

    public KneeProtection() {
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KneeProtection that = (KneeProtection) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && condition == that.condition && size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, size);
    }

    @Override
    public String toString() {
        return "KneeProtection{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition.name() +
                ", size=" + size.name() +
                '}';
    }
}
