package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.lang.model.type.DeclaredType;
import java.util.List;

@Repository
public interface EquipmentRepository<T extends Equipment> extends JpaRepository<T, Long> {

    // ----- show all -----
    List<T> findAllByTypeOrderById(TypesOfEquipment type);

    // ----- search -----
    List<T> findAllByTypeEqualsAndNameContainingIgnoreCase(TypesOfEquipment type, String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<T> findAllByTypeOrderByClothesSize(TypesOfEquipment type);

    List<T> findAllByTypeOrderBySnowboardSize(TypesOfEquipment type);

    List<T> findAllByTypeOrderBySkiSize(TypesOfEquipment type);

    List<T> findAllByTypeOrderByBootsSize(TypesOfEquipment type);
}
