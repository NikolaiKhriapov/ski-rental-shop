package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.SkiBootsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SkiBootsService {

    private final SkiBootsRepository skiBootsRepository;

    @Autowired
    public SkiBootsService(SkiBootsRepository skiBootsRepository) {
        this.skiBootsRepository = skiBootsRepository;
    }

    // ----- show all -----
    public List<SkiBoots> showAllSkiBoots() {
        return skiBootsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSkiBootsToDB(SkiBoots skiBoots) {
        skiBootsRepository.save(skiBoots);
    }

    // ----- edit -----
    public SkiBoots showOneSkiBootsById(Long id) {
        return skiBootsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("SkiBoots with id = " + id + " not found!"));
    }

    public void updateSkiBootsById(Long id, SkiBoots updatedSkiBoots) {
        SkiBoots skiBootsToBeUpdated = showOneSkiBootsById(id);

        skiBootsToBeUpdated.setName(updatedSkiBoots.getName());
        skiBootsToBeUpdated.setCondition(updatedSkiBoots.getCondition());
        skiBootsToBeUpdated.setSize(updatedSkiBoots.getSize());
        skiBootsToBeUpdated.setStiffness(updatedSkiBoots.getStiffness());

        skiBootsRepository.save(skiBootsToBeUpdated);
    }

    // ----- delete -----
    public void deleteSkiBootsById(Long id) {
        skiBootsRepository.deleteById(id);
    }

    // ----- search -----
    public List<SkiBoots> showSkiBootsBySearch(String search) {
        return skiBootsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<SkiBoots> sortAllSkiBootsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return skiBootsRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<SkiBoots> showAllAvailableSkiBoots(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all ski boots
        List<SkiBoots> listOfAvailableSkiBoots = skiBootsRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableSkiBoots.removeIf(oneSkiBoots ->
                oneSkiBoots.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneSkiBoots.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneSkiBoots.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableSkiBoots.remove(rider.getAssignedEquipment().getSkiBoots());
                }
            }
        }
        return listOfAvailableSkiBoots;
    }
}
