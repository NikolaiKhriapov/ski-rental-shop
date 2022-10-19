package my.project.skirentalshop.service;

import my.project.skirentalshop.model.AssignedEquipment;
import my.project.skirentalshop.model.Booking;
import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    @Autowired
    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    // ----- show all -----
    public List<Rider> showAllRiders() {
        return riderRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewRiderToDB(Rider rider) {
        riderRepository.save(rider);
    }

    // ----- edit -----
    public Rider showOneRiderById(Long id) {
        return riderRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Rider with id = " + id + " not found!"));
    }

    public void updateRiderById(Long id, Rider updatedRider) {
        Rider riderToBeUpdated = showOneRiderById(id);

        riderToBeUpdated.setName(updatedRider.getName());
        riderToBeUpdated.setFootSize(updatedRider.getFootSize());
        riderToBeUpdated.setSex(updatedRider.getSex());
        riderToBeUpdated.setHeight(updatedRider.getHeight());
        riderToBeUpdated.setWeight(updatedRider.getWeight());
        riderToBeUpdated.setEquipmentNeededIds(updatedRider.getEquipmentNeededIds());

        riderRepository.save(riderToBeUpdated);
    }

    // ----- delete -----
    public void deleteRiderById(Long id) {
        riderRepository.deleteById(id);
    }

    // ----- sort -----
    public List<Rider> sortAllRidersByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return riderRepository.findAll(sort);
    }

    //// ----- edit booking info / assign equipment to riders -----
    public void assignEquipmentToRider(Rider rider, AssignedEquipment assignedEquipment) {
        rider.setAssignedEquipment(assignedEquipment);
        riderRepository.save(rider);
    }

    public void removeAssignedEquipment(Rider rider) {
        rider.getAssignedEquipment().setSnowboard(null);
        rider.getAssignedEquipment().setSnowboardBoots(null);
        rider.getAssignedEquipment().setSki(null);
        rider.getAssignedEquipment().setSkiBoots(null);
        rider.getAssignedEquipment().setJacket(null);
        rider.getAssignedEquipment().setPants(null);
        rider.getAssignedEquipment().setKneeProtection(null);
        rider.getAssignedEquipment().setProtectiveShorts(null);
        rider.getAssignedEquipment().setHelmet(null);
        rider.getAssignedEquipment().setGloves(null);

        riderRepository.save(rider);
    }

    // ----- show available existing riders for client for booking -----
    public List<Rider> showAvailableExistingRidersForClientForBooking(Booking bookingToBeUpdated,
                                                                      BookingService bookingService) {
        List<Rider> allRidersForClient = new ArrayList<>();

        List<Booking> allBookingsForClient = bookingService.showAllBookingsForClient(bookingToBeUpdated.getClient().getEmail());
        List<Booking> allBookingsForClientForTheSameTime = bookingService.showBookingsForTheDate(
                bookingToBeUpdated.getDateOfArrival(), bookingToBeUpdated.getDateOfReturn());

        for (Booking oneBooking : allBookingsForClient) {
            for (Rider oneRider : oneBooking.getListOfRiders()) {
                if (!allRidersForClient.contains(oneRider)) {
                    allRidersForClient.add(oneRider);
                }
            }
        }
        for (Booking oneBooking : allBookingsForClientForTheSameTime) {
            allRidersForClient.removeAll(oneBooking.getListOfRiders());
        }
        allRidersForClient.removeAll(bookingToBeUpdated.getListOfRiders());

        return allRidersForClient;
    }
}
