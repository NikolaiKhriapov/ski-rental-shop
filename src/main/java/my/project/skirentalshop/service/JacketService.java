package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.JacketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JacketService {

    private final JacketRepository jacketRepository;

    @Autowired
    public JacketService(JacketRepository jacketRepository) {
        this.jacketRepository = jacketRepository;
    }

    // ----- show all -----
    public List<Jacket> showAllJackets() {
        return jacketRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewJacketToDB(Jacket jacket) {
        jacketRepository.save(jacket);
    }

    // ----- edit -----
    public Jacket showOneJacketById(Long id) {
        return jacketRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Jacket with id = " + id + " not found!"));
    }

    public void updateJacketById(Long id, Jacket updatedJacket) {
        Jacket jacketToBeUpdated = showOneJacketById(id);

        jacketToBeUpdated.setName(updatedJacket.getName());
        jacketToBeUpdated.setCondition(updatedJacket.getCondition());
        jacketToBeUpdated.setSize(updatedJacket.getSize());

        jacketRepository.save(jacketToBeUpdated);
    }

    // ----- delete -----
    public void deleteJacketById(Long id) {
        jacketRepository.deleteById(id);
    }

    // ----- search -----
    public List<Jacket> showJacketsBySearch(String search) {
        return jacketRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Jacket> sortAllJacketsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return jacketRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<Jacket> showAllAvailableJackets(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all jackets
        List<Jacket> listOfAvailableJackets = jacketRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableJackets.removeIf(oneJacket ->
                oneJacket.getCondition().equals(EquipmentCondition.BROKEN) ||
                oneJacket.getCondition().equals(EquipmentCondition.SERVICE) ||
                oneJacket.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableJackets.remove(rider.getAssignedEquipment().getJacket());
                }
            }
        }
        return listOfAvailableJackets;
    }
}
