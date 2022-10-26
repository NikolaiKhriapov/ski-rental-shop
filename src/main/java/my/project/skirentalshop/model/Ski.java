package my.project.skirentalshop.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Ski extends Equipment {

    @OneToMany(mappedBy = "ski")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Ski() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ski ski = (Ski) o;
        return Objects.equals(id, ski.id) &&
                Objects.equals(name, ski.name) &&
                condition == ski.condition &&
                Objects.equals(clothesSize, ski.clothesSize) &&
                stiffness == ski.stiffness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize, stiffness);
    }

    @Override
    public String toString() {
        return "Ski{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize='" + clothesSize +
                ", stiffness=" + stiffness +
                '}';
    }
}
