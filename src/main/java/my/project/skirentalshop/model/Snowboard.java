package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Snowboard extends Equipment {

    @OneToMany(mappedBy = "snowboard")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public Snowboard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snowboard snowboard = (Snowboard) o;
        return Objects.equals(id, snowboard.id) &&
                Objects.equals(name, snowboard.name) &&
                condition == snowboard.condition &&
                Objects.equals(clothesSize, snowboard.clothesSize) &&
                stiffness == snowboard.stiffness &&
                arch == snowboard.arch &&
                bindingSize == snowboard.bindingSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, condition, clothesSize, stiffness, arch, bindingSize);
    }

    @Override
    public String toString() {
        return "Snowboard{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize='" + clothesSize +
                ", stiffness=" + stiffness +
                ", arch=" + arch +
                ", bindingSize=" + bindingSize +
                '}';
    }
}
