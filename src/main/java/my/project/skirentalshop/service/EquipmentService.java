package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService<T extends Equipment> {

    private final EquipmentRepository<T> equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository<T> equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    // ----- show all -----
    public List<T> showAllEquipment() {
        return equipmentRepository.findAll();
    }
    public List<T> showAllEquipment(TypesOfEquipment type) {
        return equipmentRepository.findAllByTypeOrderById(type);
    }

    // ----- add new -----
    public void addNewEquipmentToDB(T equipment, TypesOfEquipment type) {
        equipment.setType(type);
        equipmentRepository.save(equipment);
    }

    // ----- edit -----
    public T showOneEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Equipment with id = " + id + " not found!"));
    }

    public void updateEquipmentById(Long id, T updatedEquipment, TypesOfEquipment type) {
        switch (type) {
            case SNOWBOARD -> {
                Snowboard updatedSnowboard = (Snowboard) updatedEquipment;
                Snowboard snowboardToBeUpdated = (Snowboard) showOneEquipmentById(id);

                snowboardToBeUpdated.setName(updatedSnowboard.getName());
                snowboardToBeUpdated.setCondition(updatedSnowboard.getCondition());
                snowboardToBeUpdated.setSnowboardSize(updatedSnowboard.getSnowboardSize());
                snowboardToBeUpdated.setStiffness(updatedSnowboard.getStiffness());
                snowboardToBeUpdated.setArch(updatedSnowboard.getArch());
                snowboardToBeUpdated.setBindingSize(updatedSnowboard.getBindingSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) snowboardToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case SNOWBOARD_BOOTS -> {
                SnowboardBoots updatedSnowboardBoots = (SnowboardBoots) updatedEquipment;
                SnowboardBoots snowboardBootsToBeUpdated = (SnowboardBoots) showOneEquipmentById(id);

                snowboardBootsToBeUpdated.setName(updatedSnowboardBoots.getName());
                snowboardBootsToBeUpdated.setCondition(updatedSnowboardBoots.getCondition());
                snowboardBootsToBeUpdated.setBootsSize(updatedSnowboardBoots.getBootsSize());
                snowboardBootsToBeUpdated.setStiffness(updatedSnowboardBoots.getStiffness());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) snowboardBootsToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case SKI -> {
                Ski updatedSki = (Ski) updatedEquipment;
                Ski skiToBeUpdated = (Ski) showOneEquipmentById(id);

                skiToBeUpdated.setName(updatedSki.getName());
                skiToBeUpdated.setSkiSize(updatedSki.getSkiSize());
                skiToBeUpdated.setStiffness(updatedSki.getStiffness());
                skiToBeUpdated.setCondition(updatedSki.getCondition());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) skiToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case SKI_BOOTS -> {
                SkiBoots updatedSkiBoots = (SkiBoots) updatedEquipment;
                SkiBoots skiBootsToBeUpdated = (SkiBoots) showOneEquipmentById(id);

                skiBootsToBeUpdated.setName(updatedSkiBoots.getName());
                skiBootsToBeUpdated.setCondition(updatedSkiBoots.getCondition());
                skiBootsToBeUpdated.setBootsSize(updatedSkiBoots.getBootsSize());
                skiBootsToBeUpdated.setStiffness(updatedSkiBoots.getStiffness());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) skiBootsToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case HELMET -> {
                Helmet updatedHelmet = (Helmet) updatedEquipment;
                Helmet helmetToBeUpdated = (Helmet) showOneEquipmentById(id);

                helmetToBeUpdated.setName(updatedHelmet.getName());
                helmetToBeUpdated.setClothesSize(updatedHelmet.getClothesSize());
                helmetToBeUpdated.setCondition(updatedHelmet.getCondition());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) helmetToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case JACKET -> {
                Jacket updatedJacket = (Jacket) updatedEquipment;
                Jacket jacketToBeUpdated = (Jacket) showOneEquipmentById(id);

                jacketToBeUpdated.setName(updatedJacket.getName());
                jacketToBeUpdated.setCondition(updatedJacket.getCondition());
                jacketToBeUpdated.setClothesSize(updatedJacket.getClothesSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) jacketToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case GLOVES -> {
                Gloves updatedGloves = (Gloves) updatedEquipment;
                Gloves glovesToBeUpdated = (Gloves) showOneEquipmentById(id);

                glovesToBeUpdated.setName(updatedGloves.getName());
                glovesToBeUpdated.setCondition(updatedGloves.getCondition());
                glovesToBeUpdated.setClothesSize(updatedGloves.getClothesSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) glovesToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case PANTS -> {
                Pants updatedPants = (Pants) updatedEquipment;
                Pants pantsToBeUpdated = (Pants) showOneEquipmentById(id);

                pantsToBeUpdated.setName(updatedPants.getName());
                pantsToBeUpdated.setCondition(updatedPants.getCondition());
                pantsToBeUpdated.setClothesSize(updatedPants.getClothesSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) pantsToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case PROTECTIVE_SHORTS -> {
                ProtectiveShorts updatedProtectiveShorts = (ProtectiveShorts) updatedEquipment;
                ProtectiveShorts protectiveShortsToBeUpdated = (ProtectiveShorts) showOneEquipmentById(id);

                protectiveShortsToBeUpdated.setName(updatedProtectiveShorts.getName());
                protectiveShortsToBeUpdated.setCondition(updatedProtectiveShorts.getCondition());
                protectiveShortsToBeUpdated.setClothesSize(updatedProtectiveShorts.getClothesSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) protectiveShortsToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
            case KNEE_PROTECTION -> {
                KneeProtection updatedKneeProtection = (KneeProtection) updatedEquipment;
                KneeProtection kneeProtectionToBeUpdated = (KneeProtection) showOneEquipmentById(id);

                kneeProtectionToBeUpdated.setName(updatedKneeProtection.getName());
                kneeProtectionToBeUpdated.setCondition(updatedKneeProtection.getCondition());
                kneeProtectionToBeUpdated.setClothesSize(updatedKneeProtection.getClothesSize());

                @SuppressWarnings("unchecked")
                T equipmentToBeUpdated = (T) kneeProtectionToBeUpdated;
                equipmentRepository.save(equipmentToBeUpdated);
            }
        }
    }

    // ----- delete -----
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }

    // ----- search -----
    public List<T> showEquipmentBySearch(String search, TypesOfEquipment type) {
        return equipmentRepository.findAllByTypeEqualsAndNameContainingIgnoreCase(type, search);
    }

    // ----- sort -----
    public List<T> sortAllEquipmentByParameter(String parameter, String sortDirection, TypesOfEquipment type) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        List<T> allEquipmentSortedByParameter = equipmentRepository.findAll(sort);
        allEquipmentSortedByParameter.removeIf(i -> !(i.getType().equals(type)));
        return allEquipmentSortedByParameter;
    }
}
