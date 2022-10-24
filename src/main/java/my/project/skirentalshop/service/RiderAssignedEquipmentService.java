package my.project.skirentalshop.service;

import my.project.skirentalshop.model.RiderAssignedEquipment;
import my.project.skirentalshop.repository.AssignedEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiderAssignedEquipmentService {

    private final AssignedEquipmentRepository assignedEquipmentRepository;

    @Autowired
    public RiderAssignedEquipmentService(AssignedEquipmentRepository assignedEquipmentRepository) {
        this.assignedEquipmentRepository = assignedEquipmentRepository;
    }

    public void delete(RiderAssignedEquipment riderAssignedEquipment) {
        assignedEquipmentRepository.delete(riderAssignedEquipment);
    }
}
