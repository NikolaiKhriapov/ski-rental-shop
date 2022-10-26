package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Pants extends Equipment {

    @OneToMany(mappedBy = "pants")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Pants() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pants pants = (Pants) o;
        return Objects.equals(id, pants.id) &&
                Objects.equals(name, pants.name) &&
                condition == pants.condition &&
                clothesSize == pants.clothesSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize);
    }

    @Override
    public String toString() {
        return "Pants{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
