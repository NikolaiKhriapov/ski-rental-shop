package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.SkiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SkiService {

    private final SkiRepository skiRepository;

    @Autowired
    public SkiService(SkiRepository skiRepository) {
        this.skiRepository = skiRepository;
    }

    // ----- show all -----
    public List<Ski> showAllSki() {
        return skiRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSkiToDB(Ski ski) {
        skiRepository.save(ski);
    }

    // ----- edit -----
    public Ski showOneSkiById(Long id) {
        return skiRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Ski with id = " + id + "not found!"));
    }

    public void updateSkiById(Long id, Ski updatedSki) {
        Ski skiToBeUpdated = showOneSkiById(id);

        skiToBeUpdated.setName(updatedSki.getName());
        skiToBeUpdated.setSize(updatedSki.getSize());
        skiToBeUpdated.setStiffness(updatedSki.getStiffness());
        skiToBeUpdated.setCondition(updatedSki.getCondition());

        skiRepository.save(skiToBeUpdated);
    }

    // ----- delete -----
    public void deleteSkiById(Long id) {
        skiRepository.deleteById(id);
    }

    // ----- search -----
    public List<Ski> showSkiBySearch(String search) {
        return skiRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Ski> sortAllByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return skiRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<Ski> showAllAvailableSki(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all ыki
        List<Ski> listOfAvailableSki = skiRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableSki.removeIf(oneSki ->
                oneSki.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneSki.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneSki.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableSki.remove(rider.getAssignedEquipment().getSki());
                }
            }
        }
        return listOfAvailableSki;
    }
}
