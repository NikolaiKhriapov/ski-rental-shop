package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class SnowboardBoots extends Equipment {

    @OneToMany(mappedBy = "snowboardBoots")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public SnowboardBoots() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnowboardBoots that = (SnowboardBoots) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                condition == that.condition &&
                clothesSize == that.clothesSize &&
                stiffness == that.stiffness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize, stiffness);
    }

    @Override
    public String toString() {
        return "SnowboardBoots{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                ", stiffness=" + stiffness +
                '}';
    }
}
