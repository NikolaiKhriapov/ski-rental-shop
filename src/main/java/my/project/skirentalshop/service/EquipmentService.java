package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    // ----- show all -----
    public List<Equipment> showAllEquipment(TypesOfEquipment type) {
        return equipmentRepository.findAllByTypeOrderById(type);
    }

    // ----- add new -----
    public void addNewEquipmentToDB(Equipment equipment, TypesOfEquipment type) {
        equipment.setType(type);
        equipmentRepository.save(equipment);
    }

    // ----- edit -----
    public Equipment showOneEquipmentById(Long id, TypesOfEquipment type) {
        Equipment equipment = equipmentRepository.findByIdAndType(id, type);
        if (equipment == null) {
            throw new IllegalStateException(String.format("Equipment with id=%s not found!", id));
        }
        return equipment;
    }

    public void updateEquipmentById(Long id, Equipment updatedEquipment, TypesOfEquipment type) {
        Equipment equipmentToBeUpdated = showOneEquipmentById(id, type);
        switch (type) {
            case SNOWBOARD -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
                equipmentToBeUpdated.setSize(updatedEquipment.getSize());
                equipmentToBeUpdated.setStiffness(updatedEquipment.getStiffness());
                equipmentToBeUpdated.setArch(updatedEquipment.getArch());
                equipmentToBeUpdated.setBindingSize(updatedEquipment.getBindingSize());
            }
            case SKI, SNOWBOARD_BOOTS, SKI_BOOTS -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
                equipmentToBeUpdated.setSize(updatedEquipment.getSize());
                equipmentToBeUpdated.setStiffness(updatedEquipment.getStiffness());
            }
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
                equipmentToBeUpdated.setSize(updatedEquipment.getSize());
            }
        }
        equipmentRepository.save(equipmentToBeUpdated);
    }

    // ----- delete -----
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }

    // ----- search -----
    public List<Equipment> showEquipmentBySearch(String search, TypesOfEquipment type) {
        return equipmentRepository.findAllByTypeEqualsAndNameContainingIgnoreCaseOrderById(type, search);
    }

    // ----- sort -----
    public List<Equipment> sortAllEquipmentByParameter(String parameter, String sortDirection, TypesOfEquipment type) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        List<Equipment> allEquipmentSortedByParameter = equipmentRepository.findAll(sort);
        allEquipmentSortedByParameter.removeIf(i -> !(i.getType().equals(type)));
        return allEquipmentSortedByParameter;
    }
}
