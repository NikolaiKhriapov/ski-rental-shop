package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.Booking;
import my.project.skirentalshop.model.BookingRiderEquipmentLink;
import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/client/bookings")
public class ClientBookingController {

    private final BookingService bookingService;

    @Autowired
    public ClientBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ----- showAll -----
    @GetMapping("/history")
    public String showClientHistory(Model model) {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long clientId = applicationUser.getClient().getId();

        model.addAttribute("action", "showAll");
        model.addAttribute("listOfBookingsForClient", bookingService.showAllBookingsForClient(clientId));
        return "client/booking/bookings";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewBooking(Model model) {
        Booking newBooking = new Booking();
        newBooking.setClient(bookingService.showCurrentClient());
        model.addAttribute("action", "create");
        model.addAttribute("booking", newBooking);
        return "client/booking/bookings";
    }

    @PostMapping("/add-new")
    public String addNewClientAndBookingToDB(@ModelAttribute("booking") @Valid Booking newBooking,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "client/booking/bookings";
        }
        newBooking.setClient(bookingService.showCurrentClient());
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/client/riders/add-new?bookingId=" + newBooking.getId();
    }

    // ----- edit -----
    @GetMapping("/edit/{bookingId}")
    public String showOneBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId, Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("booking", bookingService.showOneBookingById(bookingToBeUpdatedId));
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allAvailableRidersForClient",
                bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
        return "client/booking/bookings";
    }

    @PatchMapping("/edit/{bookingId}")
    public String updateBookingAndClientInfo(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                             @ModelAttribute("booking") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            bookingService.setListOfRiders(updatedBookingInfo, bookingService.getListOfRiders(bookingToBeUpdatedId));
            model.addAttribute("action", "update");
            model.addAttribute("booking", updatedBookingInfo);
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allAvailableRidersForClient",
                    bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
            return "client/booking/bookings";
        }
        bookingService.updateBookingById(bookingToBeUpdatedId, updatedBookingInfo);
        return "redirect:/client/bookings/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit-equipment/{riderId}")
    public String updateRiderRequestedEquipment(@PathVariable("riderId") Long riderToBeUpdatedId,
                                                @RequestParam("bookingId") Long bookingToBeUpdatedId,
                                                @ModelAttribute("link") BookingRiderEquipmentLink updatedLink) {
        bookingService.updateRiderRequestedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, updatedLink);
        return "redirect:/client/bookings/edit/" + bookingToBeUpdatedId;
    }

    @GetMapping("/edit/remove-rider")
    public String removeRiderFromBooking(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                         @RequestParam("riderId") Long riderToBeRemovedId) {
        bookingService.removeRiderFromBooking(bookingToBeUpdatedId, riderToBeRemovedId);
        return "redirect:/client/bookings/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/add-existing-rider/{bookingId}")
    public String addExistingRiderToBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        bookingService.addRiderToBooking(bookingToBeUpdatedId, existingRiderToBeAddedId);
        return "redirect:/client/bookings/edit/" + bookingToBeUpdatedId;
    }

    // ----- delete -----
    @DeleteMapping("/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBookingById(bookingId);
        return "redirect:/client";
    }
}
