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
    public Equipment showOneEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Equipment with id = " + id + " not found!"));
    }

    public void updateEquipmentById(Long id, Equipment updatedEquipment, TypesOfEquipment type) {
        Equipment equipmentToBeUpdated = showOneEquipmentById(id);
        switch (type) {
            case SNOWBOARD -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
                equipmentToBeUpdated.setSnowboardSize(updatedEquipment.getSnowboardSize());
                equipmentToBeUpdated.setStiffness(updatedEquipment.getStiffness());
                equipmentToBeUpdated.setArch(updatedEquipment.getArch());
                equipmentToBeUpdated.setBindingSize(updatedEquipment.getBindingSize());
            }
            case SNOWBOARD_BOOTS, SKI_BOOTS -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
                equipmentToBeUpdated.setBootsSize(updatedEquipment.getBootsSize());
                equipmentToBeUpdated.setStiffness(updatedEquipment.getStiffness());
            }
            case SKI -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setSkiSize(updatedEquipment.getSkiSize());
                equipmentToBeUpdated.setStiffness(updatedEquipment.getStiffness());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
            }
            case HELMET, JACKET, GLOVES, PANTS, PROTECTIVE_SHORTS, KNEE_PROTECTION -> {
                equipmentToBeUpdated.setName(updatedEquipment.getName());
                equipmentToBeUpdated.setClothesSize(updatedEquipment.getClothesSize());
                equipmentToBeUpdated.setCondition(updatedEquipment.getCondition());
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
        return equipmentRepository.findAllByTypeEqualsAndNameContainingIgnoreCase(type, search);
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
