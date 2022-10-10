package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Gloves extends Equipment {

    enum Size {XS, S, M, L, XL}

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @javax.validation.constraints.Size(min = 3, max = 30, message = "{validation.gloves.invalid_name}")
    private String name;
    private Size size;
    private EquipmentCondition condition;
    @OneToMany(mappedBy = "gloves")
    private List<AssignedEquipment> listOfAssignedEquipment;

    public Gloves() {
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public EquipmentCondition getCondition() {
        return condition;
    }

    public void setCondition(EquipmentCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gloves gloves = (Gloves) o;
        return Objects.equals(id, gloves.id) && Objects.equals(name, gloves.name) &&
                size == gloves.size && condition == gloves.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, condition);
    }

    @Override
    public String toString() {
        return "Gloves{" +
                "id=" + id +
                ", name='" + name +
                ", size=" + size.name() +
                ", condition=" + condition.name() +
                '}';
    }
}
