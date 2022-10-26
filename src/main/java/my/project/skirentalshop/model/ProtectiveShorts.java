package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class ProtectiveShorts extends Equipment {

    @OneToMany(mappedBy = "protectiveShorts")
    private List<RiderAssignedEquipment> listOfRiderAssignedEquipment;

    public ProtectiveShorts() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtectiveShorts that = (ProtectiveShorts) o;
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
        return "ProtectiveShorts{" +
                "id=" + id +
                ", name='" + name +
                ", condition=" + condition +
                ", clothesSize=" + clothesSize +
                '}';
    }
}
