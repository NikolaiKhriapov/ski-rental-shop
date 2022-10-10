package my.project.skirentalshop.repository;

import my.project.skirentalshop.model.AssignedEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedEquipmentRepository extends JpaRepository<AssignedEquipment, Long> {
}
