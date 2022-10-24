package my.project.skirentalshop.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
public class Gloves {

    enum Size {XS, S, M, L, XL}

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @NotBlank(message = "{validation.equipment.invalid_name.not_empty}")
    @javax.validation.constraints.Size(message = "{validation.equipment.invalid_name.size}")
    private String name;

    private EquipmentCondition condition;

    private Size size;

    @OneToMany(mappedBy = "gloves")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

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
        Gloves gloves = (Gloves) o;
        return Objects.equals(id, gloves.id) &&
                Objects.equals(name, gloves.name) &&
                condition == gloves.condition &&
                size == gloves.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, size);
    }

    @Override
    public String toString() {
        return "Gloves{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", size=" + size +
                '}';
    }
}
