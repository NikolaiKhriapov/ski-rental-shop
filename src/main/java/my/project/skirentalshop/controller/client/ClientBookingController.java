package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.Booking;
import my.project.skirentalshop.model.BookingRiderEquipmentLink;
import my.project.skirentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/client/info-booking")
public class ClientBookingController {

    private final BookingService bookingService;

    @Autowired
    public ClientBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ----- add new booking -----
    @GetMapping("/add-new")
    public String createNewBooking(Model model) {
        Booking newBooking = new Booking();
        newBooking.setClient(bookingService.showCurrentClient());
        model.addAttribute("newBooking", newBooking);
        return "client/booking/add_new";
    }

    @PostMapping("/add-new")
    public String addNewClientAndBookingToDB(@ModelAttribute("newBooking") @Valid Booking newBooking,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/booking/add_new";
        }
        newBooking.setClient(bookingService.showCurrentClient());
        bookingService.addNewBookingToDB(newBooking);
        return "redirect:/client/info-riders/add-new?bookingId=" + newBooking.getId();
    }

    // ----- edit booking info -----
    @GetMapping("/edit/{bookingId}")
    public String showOneBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId, Model model) {
        model.addAttribute("bookingToBeUpdated", bookingService.showOneBookingById(bookingToBeUpdatedId));
        model.addAttribute("existingRiderToBeAddedId", 0L);
        model.addAttribute("allAvailableRidersForClient",
                bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
        return "client/booking/edit";
    }

    @PatchMapping("/edit/{bookingId}")
    public String updateBookingAndClientInfo(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                             @ModelAttribute("bookingToBeUpdated") @Valid Booking updatedBookingInfo,
                                             BindingResult bindingResult,
                                             Model model) {
        if (bindingResult.hasErrors()) {
            bookingService.setListOfRiders(updatedBookingInfo, bookingService.getListOfRiders(bookingToBeUpdatedId));
            model.addAttribute("bookingToBeUpdated", updatedBookingInfo);
            model.addAttribute("existingRiderToBeAddedId", 0L);
            model.addAttribute("allAvailableRidersForClient",
                    bookingService.showAvailableExistingRidersForClientForBooking(bookingToBeUpdatedId));
            return "client/booking/edit";
        }
        bookingService.updateBookingById(bookingToBeUpdatedId, updatedBookingInfo);
        return "redirect:/client/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit-equipment/{riderId}")
    public String updateRiderRequestedEquipment(@PathVariable("riderId") Long riderToBeUpdatedId,
                                                @RequestParam("bookingId") Long bookingToBeUpdatedId,
                                                @ModelAttribute("link") BookingRiderEquipmentLink updatedLink) {
        bookingService.updateRiderRequestedEquipment(bookingToBeUpdatedId, riderToBeUpdatedId, updatedLink);
        return "redirect:/client/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @GetMapping("/edit/remove-rider")
    public String removeRiderFromBooking(@RequestParam("bookingId") Long bookingToBeUpdatedId,
                                         @RequestParam("riderId") Long riderToBeRemovedId) {
        bookingService.removeRiderFromBooking(bookingToBeUpdatedId, riderToBeRemovedId);
        return "redirect:/client/info-booking/edit/" + bookingToBeUpdatedId;
    }

    @PatchMapping("/edit/add-existing-rider/{bookingId}")
    public String addExistingRiderToBooking(@PathVariable("bookingId") Long bookingToBeUpdatedId,
                                            @ModelAttribute("existingRiderToBeAddedId") Long existingRiderToBeAddedId) {
        bookingService.addRiderToBooking(bookingToBeUpdatedId, existingRiderToBeAddedId);
        return "redirect:/client/info-booking/edit/" + bookingToBeUpdatedId;
    }

    // ----- delete booking -----
    @DeleteMapping("/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBookingById(bookingId);
        return "redirect:/client";
    }
}
