package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // ----- show all -----
    List<Equipment> findAllByTypeOrderById(TypesOfEquipment type);

    // ----- edit -----
    Equipment findByIdAndType(Long id, TypesOfEquipment type);

    // ----- search -----
    List<Equipment> findAllByTypeEqualsAndNameContainingIgnoreCaseOrderById(TypesOfEquipment type, String partOfName);

    //// ----- edit booking info / assign equipment to riders -----
    List<Equipment> findAllByTypeOrderBySize(TypesOfEquipment type);
}
