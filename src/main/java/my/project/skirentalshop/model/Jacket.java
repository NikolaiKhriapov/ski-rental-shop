package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Jacket extends Equipment {

    @OneToMany(mappedBy = "jacket")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Jacket() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jacket jacket = (Jacket) o;
        return Objects.equals(id, jacket.id) &&
                Objects.equals(name, jacket.name) &&
                condition == jacket.condition &&
                clothesSize == jacket.clothesSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize);
    }

    @Override
    public String toString() {
        return "Jacket{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
