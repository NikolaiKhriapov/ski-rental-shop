package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Pants {

    enum Size {XS, S, M, L, XL}

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @javax.validation.constraints.Size(min = 3, max = 30, message = "{validation.pants.invalid_name}")
    private String name;
    private Size size;
    private EquipmentCondition condition;
    @OneToMany(mappedBy = "pants")
    private List<AssignedEquipment> listOfAssignedEquipment;

    public Pants() {
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
        Pants pants = (Pants) o;
        return Objects.equals(id, pants.id) && Objects.equals(name, pants.name) && size == pants.size && condition == pants.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, condition);
    }

    @Override
    public String toString() {
        return "Pants{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition.name() +
                ", size=" + size.name() +
                '}';
    }
}
