package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.HelmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HelmetService {

    private final HelmetRepository helmetRepository;

    @Autowired
    public HelmetService(HelmetRepository helmetRepository) {
        this.helmetRepository = helmetRepository;
    }

    // ----- show all -----
    public List<Helmet> showAllHelmets() {
        return helmetRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewHelmetToDB(Helmet helmet) {
        helmetRepository.save(helmet);
    }

    // ----- edit -----
    public Helmet showOneHelmetById(Long id) {
        return helmetRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Helmet with id = " + id + " not found!"));
    }

    public void updateHelmetById(Long id, Helmet updatedHelmet) {
        Helmet helmetToBeUpdated = showOneHelmetById(id);

        helmetToBeUpdated.setName(updatedHelmet.getName());
        helmetToBeUpdated.setSize(updatedHelmet.getSize());
        helmetToBeUpdated.setCondition(updatedHelmet.getCondition());

        helmetRepository.save(helmetToBeUpdated);
    }

    // ----- delete -----
    public void deleteHelmetById(Long id) {
        helmetRepository.deleteById(id);
    }

    // ----- search -----
    public List<Helmet> showHelmetsBySearch(String search) {
        return helmetRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Helmet> sortAllHelmetsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return helmetRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<Helmet> showAllAvailableHelmets(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all helmets
        List<Helmet> listOfAvailableHelmets = helmetRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableHelmets.removeIf(oneHelmet ->
                oneHelmet.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneHelmet.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneHelmet.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableHelmets.remove(rider.getAssignedEquipment().getHelmet());
                }
            }
        }
        return listOfAvailableHelmets;
    }
}