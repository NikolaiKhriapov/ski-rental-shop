package my.project.skirentalshop.repository;

import my.project.skirentalshop.entity.Equipment;
import my.project.skirentalshop.entity.enums.EquipmentCondition;
import my.project.skirentalshop.entity.enums.TypesOfEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findAllByTypeOrderById(TypesOfEquipment type);

    Equipment findByIdAndType(Long id, TypesOfEquipment type);

    List<Equipment> findAllByTypeEqualsAndNameContainingIgnoreCaseOrderById(TypesOfEquipment type, String partOfName);

    List<Equipment> findAllByTypeOrderBySize(TypesOfEquipment type);

    List<Equipment> findAllByConditionOrderByType(EquipmentCondition condition);

}
