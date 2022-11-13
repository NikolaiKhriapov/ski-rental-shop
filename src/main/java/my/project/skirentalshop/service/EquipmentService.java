package my.project.skirentalshop.service;

import my.project.skirentalshop.entity.*;
import my.project.skirentalshop.entity.enums.EquipmentCondition;
import my.project.skirentalshop.entity.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    // ----- show all -----
    public List<Equipment> showAllEquipmentByType(String type) {
        return equipmentRepository.findAllByTypeOrderById(TypesOfEquipment.convertToEnumField(type));
    }

    // ----- add new -----
    public Equipment createNewEquipmentByType(String type) {
        Equipment equipment = new Equipment();
        equipment.setType(TypesOfEquipment.convertToEnumField(type));
        return equipment;
    }

    public void addNewEquipmentToDB(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    // ----- edit -----
    public Equipment showOneEquipmentById(Long id, String type) {
        Equipment equipment = equipmentRepository.findByIdAndType(id, TypesOfEquipment.convertToEnumField(type));
        if (equipment == null) {
            throw new IllegalStateException(
                    getExceptionMessage("exception.equipment.id-not-found", id.toString())
            );
        }
        return equipment;
    }

    public void updateEquipmentById(Long id, Equipment updatedEquipment, String type) {
        Equipment equipmentToBeUpdated = showOneEquipmentById(id, type);
        switch ((Objects.requireNonNull(TypesOfEquipment.convertToEnumField(type)))) {
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
    public List<Equipment> showEquipmentBySearch(String search, String type) {
        return equipmentRepository.findAllByTypeEqualsAndNameContainingIgnoreCaseOrderById(
                TypesOfEquipment.convertToEnumField(type), search
        );
    }

    // ----- sort -----
    public List<Equipment> sortAllEquipmentByParameter(String parameter, String sortDirection, String type) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        List<Equipment> allEquipmentSortedByParameter = equipmentRepository.findAll(sort);

        allEquipmentSortedByParameter
                .removeIf(oneEquipment -> !(oneEquipment.getType().equals(TypesOfEquipment.convertToEnumField(type))));
        return allEquipmentSortedByParameter;
    }

    // ----- show broken equipment -----
    public List<Equipment> showEquipmentByCondition(EquipmentCondition condition) {
        return equipmentRepository.findAllByConditionOrderByType(condition);
    }

    // ----- supplementary -----
    public String getExceptionMessage(String propertyKey, String parameter) {
        return String.format(
                ResourceBundle
                        .getBundle("exception", LocaleContextHolder.getLocale())
                        .getString(propertyKey),
                parameter
        );
    }

}
