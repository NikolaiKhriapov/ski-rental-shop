package my.project.skirentalshop.service;

import my.project.skirentalshop.model.Booking;
import my.project.skirentalshop.model.EquipmentCondition;
import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.repository.SnowboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SnowboardService {

    private final SnowboardRepository snowboardRepository;

    @Autowired
    public SnowboardService(SnowboardRepository snowboardRepository) {
        this.snowboardRepository = snowboardRepository;
    }

    // ----- show all -----
    public List<Snowboard> showAllSnowboards() {
        return snowboardRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSnowboardToDB(Snowboard snowboard) {
        snowboardRepository.save(snowboard);
    }

    // ----- edit -----
    public Snowboard showOneSnowboardById(Long id) {
        return snowboardRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Snowboard with id = " + id + " not found!"));
    }

    public void updateSnowboardById(Long id, Snowboard updatedSnowboard) {
        Snowboard snowboardToBeUpdated = showOneSnowboardById(id);

        snowboardToBeUpdated.setName(updatedSnowboard.getName());
        snowboardToBeUpdated.setCondition(updatedSnowboard.getCondition());
        snowboardToBeUpdated.setSize(updatedSnowboard.getSize());
        snowboardToBeUpdated.setStiffness(updatedSnowboard.getStiffness());
        snowboardToBeUpdated.setArch(updatedSnowboard.getArch());
        snowboardToBeUpdated.setBindingSize(updatedSnowboard.getBindingSize());

        snowboardRepository.save(snowboardToBeUpdated);
    }

    // ----- delete -----
    public void deleteSnowboardById(Long id) {
        snowboardRepository.deleteById(id);
    }

    // ----- search -----
    public List<Snowboard> showSnowboardsBySearch(String search) {
        return snowboardRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Snowboard> sortAllSnowboardsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return snowboardRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<Snowboard> showAllAvailableSnowboards(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all snowboards
        List<Snowboard> listOfAvailableSnowboards = snowboardRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableSnowboards.removeIf(oneSnowboard ->
                oneSnowboard.getCondition().equals(EquipmentCondition.BROKEN) ||
                        oneSnowboard.getCondition().equals(EquipmentCondition.SERVICE) ||
                        oneSnowboard.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableSnowboards.remove(rider.getAssignedEquipment().getSnowboard());
                }
            }
        }
        return listOfAvailableSnowboards;
    }
}
