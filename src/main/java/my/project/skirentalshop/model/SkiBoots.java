package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Entity
public class SkiBoots extends Equipment {

    @OneToMany(mappedBy = "skiBoots")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public SkiBoots() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkiBoots skiBoots = (SkiBoots) o;
        return Objects.equals(id, skiBoots.id) &&
                Objects.equals(name, skiBoots.name) &&
                condition == skiBoots.condition &&
                clothesSize == skiBoots.clothesSize &&
                stiffness == skiBoots.stiffness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize, stiffness);
    }

    @Override
    public String toString() {
        return "SkiBoots{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                ", stiffness=" + stiffness +
                '}';
    }
}
