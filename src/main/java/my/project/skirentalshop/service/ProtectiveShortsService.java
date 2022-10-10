package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.ProtectiveShortsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProtectiveShortsService {

    private final ProtectiveShortsRepository protectiveShortsRepository;

    @Autowired
    public ProtectiveShortsService(ProtectiveShortsRepository protectiveShortsRepository) {
        this.protectiveShortsRepository = protectiveShortsRepository;
    }

    // ----- show all -----
    public List<ProtectiveShorts> showAllProtectiveShorts() {
        return protectiveShortsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewProtectiveShortsToDB(ProtectiveShorts protectiveShorts) {
        protectiveShortsRepository.save(protectiveShorts);
    }

    // ----- edit -----
    public ProtectiveShorts showOneProtectiveShortsById(Long id) {
        return protectiveShortsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("ProtectiveShorts with id = " + id + " not found!"));
    }

    public void updateProtectiveShortsById(Long id, ProtectiveShorts updatedProtectiveShorts) {
        ProtectiveShorts protectiveShortsToBeUpdated = showOneProtectiveShortsById(id);

        protectiveShortsToBeUpdated.setName(updatedProtectiveShorts.getName());
        protectiveShortsToBeUpdated.setCondition(updatedProtectiveShorts.getCondition());
        protectiveShortsToBeUpdated.setSize(updatedProtectiveShorts.getSize());

        protectiveShortsRepository.save(protectiveShortsToBeUpdated);
    }

    // ----- delete -----
    public void deleteProtectiveShortsById(Long id) {
        protectiveShortsRepository.deleteById(id);
    }

    // ----- search -----
    public List<ProtectiveShorts> showProtectiveShortsBySearch(String search) {
        return protectiveShortsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<ProtectiveShorts> sortAllProtectiveShortsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return protectiveShortsRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public List<ProtectiveShorts> showAllAvailableProtectiveShorts(Date dateOfArrival, Date dateOfReturn, List<Booking> allBookings) {
        //get all protective shorts
        List<ProtectiveShorts> listOfAvailableProtectiveShorts = protectiveShortsRepository.findAllByOrderBySize();
        //remove equipment that is broken, in service, or otherwise not ready
        listOfAvailableProtectiveShorts.removeIf(oneProtectiveShorts ->
                oneProtectiveShorts.getCondition().equals(EquipmentCondition.BROKEN) ||
                oneProtectiveShorts.getCondition().equals(EquipmentCondition.SERVICE) ||
                oneProtectiveShorts.getCondition().equals(EquipmentCondition.UNKNOWN));
        //remove already assigned equipment
        for (Booking booking : allBookings) {
            if (((dateOfArrival.after(booking.getDateOfArrival()) || dateOfArrival.equals(booking.getDateOfArrival())) &&
                    (dateOfArrival.before(booking.getDateOfReturn()) || dateOfArrival.equals(booking.getDateOfReturn()))) ||
                    ((dateOfReturn.after(booking.getDateOfArrival()) || dateOfReturn.equals(booking.getDateOfArrival())) &&
                            (dateOfReturn.before(booking.getDateOfReturn()) || dateOfReturn.equals(booking.getDateOfReturn()))) ||
                    (dateOfArrival.before(booking.getDateOfArrival()) && dateOfReturn.after(booking.getDateOfReturn()))) {
                for (Rider rider : booking.getListOfRiders()) {
                    listOfAvailableProtectiveShorts.remove(rider.getAssignedEquipment().getProtectiveShorts());
                }
            }
        }
        return listOfAvailableProtectiveShorts;
    }
}
