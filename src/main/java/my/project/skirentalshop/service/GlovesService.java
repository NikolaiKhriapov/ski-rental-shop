package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.GlovesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GlovesService {

    private final GlovesRepository glovesRepository;

    @Autowired
    public GlovesService(GlovesRepository glovesRepository) {
        this.glovesRepository = glovesRepository;
    }

    // ----- show all -----
    public List<Gloves> showAllGloves() {
        return glovesRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewGlovesToDB(Gloves gloves) {
        glovesRepository.save(gloves);
    }

    // ----- edit -----
    public Gloves showOneGlovesById(Long id) {
        return glovesRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Gloves with id = " + id + "not found"));
    }

    public void updateGlovesById(Long id, Gloves updatedGloves) {
        Gloves glovesToBeUpdated = showOneGlovesById(id);

        glovesToBeUpdated.setName(updatedGloves.getName());
        glovesToBeUpdated.setCondition(updatedGloves.getCondition());
        glovesToBeUpdated.setSize(updatedGloves.getSize());

        glovesRepository.save(glovesToBeUpdated);
    }

    // ----- delete -----
    public void deleteGlovesById(Long id) {
        glovesRepository.deleteById(id);
    }

    // ----- search -----
    public List<Gloves> showGlovesBySearch(String search) {
        return glovesRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Gloves> sortAllGlovesByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return glovesRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<Gloves> showAllAvailableGloves(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all gloves
        List<Gloves> listOfAvailableGloves = glovesRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableGloves.removeIf(oneGloves ->
                oneGloves.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneGloves.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneGloves.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableGloves.remove(rider.getAssignedEquipment().getGloves());
                }
            }
        }
        return listOfAvailableGloves;
    }
}
