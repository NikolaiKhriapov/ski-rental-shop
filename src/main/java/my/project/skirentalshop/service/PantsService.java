package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.PantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PantsService {

    private final PantsRepository pantsRepository;

    @Autowired
    public PantsService(PantsRepository pantsRepository) {
        this.pantsRepository = pantsRepository;
    }

    // ----- show all -----
    public List<Pants> showAllPants() {
        return pantsRepository.findAllByOrderById();
    }

    //------add new-----
    public void addNewPantsToDB(Pants pants) {
        pantsRepository.save(pants);
    }

    // ----- edit -----
    public Pants showOnePantsById(Long id) {
        return pantsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Pants with id = " + id + "not found!"));
    }

    public void updatePantsById(Long id, Pants updatedPants) {
        Pants pantsToBeUpdated = showOnePantsById(id);

        pantsToBeUpdated.setName(updatedPants.getName());
        pantsToBeUpdated.setCondition(updatedPants.getCondition());
        pantsToBeUpdated.setSize(updatedPants.getSize());

        pantsRepository.save(pantsToBeUpdated);
    }

    //------delete------
    public void deletePantsById(Long id) {
        pantsRepository.deleteById(id);
    }

    // ----- search -----
    public List<Pants> showPantsBySearch(String search) {
        return pantsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Pants> sortAllPantsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return pantsRepository.findAll(sort);
    }


    //// ----- edit booking info / assign equipment to riders -----
    public List<Pants> showAllAvailablePants(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all pants
        List<Pants> listOfAvailablePants = pantsRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailablePants.removeIf(onePants ->
                onePants.getCondition().equals(EquipmentCondition.BROKEN) ||
                        onePants.getCondition().equals(EquipmentCondition.SERVICE) ||
                        onePants.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailablePants.remove(rider.getAssignedEquipment().getPants());
                }
            }
        }
        return listOfAvailablePants;
    }
}
