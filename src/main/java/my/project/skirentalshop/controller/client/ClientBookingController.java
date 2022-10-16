package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/client/info-booking")
public class ClientBookingController {

    private final BookingService bookingService;
    private final ClientService clientService;
    private final RiderService riderService;

    @Autowired
    public ClientBookingController(BookingService bookingService, ClientService clientService, RiderService riderService) {
        this.bookingService = bookingService;
        this.clientService = clientService;
        this.riderService = riderService;
    }

    // ----- add new booking -----
    @GetMapping("/add-new")
    public String createNewBooking(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Booking newBooking = new Booking();
        newBooking.setClient(clientService.showOneClientByEmail(username));
        model.addAttribute("newBooking", newBooking);
        return "client/booking/add_new";
    }

    @PostMapping("")
    public String addNewClientAndBookingToDB(@ModelAttribute("newBooking") @Valid Booking newBooking,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/booking/add_new";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client currentClient = clientService.showOneClientByEmail(username);
        newBooking.setClient(currentClient);
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/client/info-riders/add-new?id=" + newBooking.getId();
    }

    // ----- edit booking info -----
    @GetMapping("/edit/{id}")
    public String showOneBooking(@PathVariable("id") Long bookingToBeUpdatedId, Model model) {
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        List<Rider> allRidersForClient =
                riderService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdated, bookingService);

        model.addAttribute("bookingToBeUpdated", bookingService.showOneBookingById(bookingToBeUpdatedId));
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allRidersForClient", allRidersForClient);
        return "client/booking/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateBookingAndClientInfo(@PathVariable("id") Long bookingToBeUpdatedId,
                                             @ModelAttribute("bookingToBeUpdated") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult, Model model) {
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        List<Booking> allBookingsForClient = bookingService.showAllBookingsForClient(bookingToBeUpdated.getClient().getEmail());
        List<Rider> allRidersForClient = new ArrayList<>();
        for (Booking oneBooking : allBookingsForClient) {
            for (Rider oneRider : oneBooking.getListOfRiders()) {
                if (!allRidersForClient.contains(oneRider)) {
                    allRidersForClient.add(oneRider);
                }
            }
        }

        if (bindingResult.hasErrors()) {
            updatedBookingInfo.setListOfRiders(bookingToBeUpdated.getListOfRiders());
            model.addAttribute("bookingToBeUpdated", updatedBookingInfo);
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allRidersForClient", allRidersForClient);
            return "client/booking/edit";
        }
        clientService.updateClientById(bookingToBeUpdated.getClient().getId(), updatedBookingInfo.getClient());
        bookingService.updateBookingById(bookingToBeUpdatedId, new Booking(bookingToBeUpdated.getClient(),
                updatedBookingInfo.getDateOfArrival(), updatedBookingInfo.getDateOfReturn()));
        return "redirect:/client/info-booking/edit/{id}";
    }

    @GetMapping("/edit/remove")
    public String removeRiderFromBooking(@RequestParam("bid") Long bookingToBeUpdatedId,
                                         @RequestParam("rid") Long riderToBeRemovedId) {
        Rider riderToBoUpdated = riderService.showOneRiderById(riderToBeRemovedId);
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);

        riderService.removeAssignedEquipment(riderToBoUpdated);
        bookingService.removeRiderFromBooking(bookingToBeUpdated, riderToBoUpdated);
        return "redirect:/client/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/add-existing-rider/{id}")
    public String addExistingRiderToBooking(@PathVariable("id") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        Rider existingRiderToBeAdded = riderService.showOneRiderById(existingRiderToBeAddedId);
        Booking bookingToBeUpdated = bookingService.showOneBookingById(bookingToBeUpdatedId);
        bookingService.addExistingRiderToBooking(bookingToBeUpdated, existingRiderToBeAdded);
        return "redirect:/client/info-booking/edit/{id}";
    }

    // ----- delete booking -----
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable("id") Long id) {
        for (Rider rider : bookingService.showOneBookingById(id).getListOfRiders()) {
            riderService.removeAssignedEquipment(rider);
        }
        bookingService.deleteBookingById(id);
        return "redirect:/client";
    }
}
