package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.Booking;
import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.service.BookingService;
import my.project.skirentalshop.service.RiderService;
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
@RequestMapping("/client/info-riders")
public class ClientRiderController {

    private final BookingService bookingService;
    private final RiderService riderService;

    @Autowired
    public ClientRiderController(BookingService bookingService, RiderService riderService) {
        this.riderService = riderService;
        this.bookingService = bookingService;
    }

    //show all
    @GetMapping()
    public String showAllRidersForClient(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Booking> allBookingsForClient = bookingService.showAllBookingsForClient(username);

        List<Rider> allRidersForClient = new ArrayList<>();
        for (Booking oneBooking : allBookingsForClient) {
            for (Rider oneRider : oneBooking.getListOfRiders()) {
                if (!allRidersForClient.contains(oneRider)) {
                    allRidersForClient.add(oneRider);
                }
            }
        }
        model.addAttribute("allRidersForClient", allRidersForClient);
        return "client/rider/show_all";
    }

    // ----- add new rider -----
    @GetMapping("/add-new")
    public String createNewRider(Model model, @RequestParam(value = "id", required = false) Long bookingId) {
        model.addAttribute("newRider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return "client/rider/add_new";
    }

    @PostMapping("/add")
    public String addNewRiderToDb(@RequestParam(value = "id", required = false) Long bookingId,
                                  @ModelAttribute("newRider") @Valid Rider rider,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/rider/add_new";
        }
        riderService.addNewRiderToDB(rider);

        if (bookingId != null) {
            bookingService.addNewRiderToBooking(bookingId, rider);
            return "redirect:/client/info-riders/add-new?id=" + bookingId;
        }
        return "redirect:/client/info-riders/";
    }

    // ----- edit -----
    @GetMapping("/edit")
    public String showOneRider(@RequestParam("id") Long id,
                               @RequestParam(value = "bookingId", required = false) Long bookingId,
                               Model model) {
        model.addAttribute("riderToUpdate", riderService.showOneRiderById(id));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", id);
        return "client/rider/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateRider(@PathVariable("id") Long riderToBeUpdatedId,
                              @RequestParam(value = "bookingId", required = false) Long bookingId,
                              @Valid @ModelAttribute("riderToUpdate") Rider updatedRider,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("riderId", riderToBeUpdatedId);
            model.addAttribute("bookingId", bookingId);
            return "client/rider/edit";
        }
        riderService.updateRiderById(riderToBeUpdatedId, updatedRider);
        if (bookingId != null) {
            return "redirect:/client/info-booking/edit/" + bookingId;
        } else {
            return "redirect:/client/info-riders";
        }
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteRider(@PathVariable("id") Long id) {
        riderService.deleteRiderById(id);
        return "redirect:/client/info-riders";
    }
}
