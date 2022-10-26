package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class KneeProtection extends Equipment {

    @OneToMany(mappedBy = "kneeProtection")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public KneeProtection() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KneeProtection that = (KneeProtection) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                condition == that.condition &&
                clothesSize == that.clothesSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize);
    }

    @Override
    public String toString() {
        return "KneeProtection{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
