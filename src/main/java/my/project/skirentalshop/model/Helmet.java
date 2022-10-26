package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Helmet extends Equipment {

    @OneToMany(mappedBy = "helmet")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Helmet() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Helmet helmet = (Helmet) o;
        return Objects.equals(id, helmet.id) &&
                Objects.equals(name, helmet.name) &&
                condition == helmet.condition &&
                clothesSize == helmet.clothesSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize);
    }

    @Override
    public String toString() {
        return "Helmet{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
