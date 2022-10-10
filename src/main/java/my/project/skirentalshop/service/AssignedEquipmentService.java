package my.project.skirentalshop.service;

import my.project.skirentalshop.model.AssignedEquipment;
import my.project.skirentalshop.repository.AssignedEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignedEquipmentService {

    private final AssignedEquipmentRepository assignedEquipmentRepository;

    @Autowired
    public AssignedEquipmentService(AssignedEquipmentRepository assignedEquipmentRepository) {
        this.assignedEquipmentRepository = assignedEquipmentRepository;
    }

    public void addNewAssignedEquipmentToDB(AssignedEquipment assignedEquipment) {
        assignedEquipmentRepository.save(assignedEquipment);
    }
}
