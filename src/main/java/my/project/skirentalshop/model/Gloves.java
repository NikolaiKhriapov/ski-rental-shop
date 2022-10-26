package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Gloves extends Equipment {

    @OneToMany(mappedBy = "gloves")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Gloves() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gloves gloves = (Gloves) o;
        return Objects.equals(id, gloves.id) &&
                Objects.equals(name, gloves.name) &&
                condition == gloves.condition &&
                clothesSize == gloves.clothesSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize);
    }

    @Override
    public String toString() {
        return "Gloves{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
