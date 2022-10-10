package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.SnowboardBootsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SnowboardBootsService {

    private final SnowboardBootsRepository snowboardBootsRepository;

    @Autowired
    public SnowboardBootsService(SnowboardBootsRepository snowboardBootsRepository) {
        this.snowboardBootsRepository = snowboardBootsRepository;
    }

    // ----- show all -----
    public List<SnowboardBoots> showAllSnowboardBoots() {
        return snowboardBootsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSnowboardBootsToDB(SnowboardBoots snowboardBoots) {
        snowboardBootsRepository.save(snowboardBoots);
    }

    // ----- edit -----
    public SnowboardBoots showOneSnowboardBootsById(Long id) {
        return snowboardBootsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("snowboardBoots with id = " + id + " not found!"));
    }

    public void updateSnowboardBootsById(Long id, SnowboardBoots updatedSnowboardBoots) {
        SnowboardBoots snowboardBootsToBeUpdated = showOneSnowboardBootsById(id);

        snowboardBootsToBeUpdated.setName(updatedSnowboardBoots.getName());
        snowboardBootsToBeUpdated.setCondition(updatedSnowboardBoots.getCondition());
        snowboardBootsToBeUpdated.setSize(updatedSnowboardBoots.getSize());
        snowboardBootsToBeUpdated.setStiffness(updatedSnowboardBoots.getStiffness());

        snowboardBootsRepository.save(snowboardBootsToBeUpdated);
    }

    // ----- delete -----
    public void deleteSnowboardBootsById(Long id) {
        snowboardBootsRepository.deleteById(id);
    }

    // ----- search -----
    public List<SnowboardBoots> showSnowboardBootsBySearch(String search) {
        return snowboardBootsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<SnowboardBoots> sortAllSnowboardBootsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return snowboardBootsRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<SnowboardBoots> showAllAvailableSnowboardBoots(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all snowboard boots
        List<SnowboardBoots> listOfAvailableSnowboardBoots = snowboardBootsRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableSnowboardBoots.removeIf(oneSnowboardBoots ->
                oneSnowboardBoots.getCondition().equals(EquipmentCondition.BROKEN) ||
                oneSnowboardBoots.getCondition().equals(EquipmentCondition.SERVICE) ||
                oneSnowboardBoots.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableSnowboardBoots.remove(rider.getAssignedEquipment().getSnowboardBoots());
                }
            }
        }
        return listOfAvailableSnowboardBoots;
    }
}
