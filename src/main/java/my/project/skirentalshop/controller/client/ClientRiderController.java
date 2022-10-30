package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/client/riders")
public class ClientRiderController {

    private final RiderService riderService;

    @Autowired
    public ClientRiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllRidersForClient(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfRidersForClient", riderService.showAllRidersForCurrentClient());
        return "client/rider/riders";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewRider(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                 Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("rider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return "client/rider/riders";
    }

    @PostMapping("/add-new")
    public String addNewRiderToDb(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                  @ModelAttribute("rider") @Valid Rider rider,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "client/rider/riders";
        }
        riderService.addNewRiderToDB(rider);

        if (bookingId != null) {
            riderService.addRiderToBooking(bookingId, rider);
            return "redirect:/client/riders/add-new?bookingId=" + bookingId;
        }
        return "redirect:/client/riders/";
    }

    // ----- edit -----
    @GetMapping("/edit")
    public String showOneRider(@RequestParam("riderId") Long riderId,
                               @RequestParam(value = "bookingId", required = false) Long bookingId,
                               Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("rider", riderService.showOneRiderById(riderId));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", riderId);
        if (bookingId != null) {
            model.addAttribute("link", riderService.getBookingRiderEquipmentLink(
                    riderService.showOneBookingById(bookingId), riderService.showOneRiderById(riderId)));
        }
        return "client/rider/riders";
    }

    @PatchMapping("/edit/{riderId}")
    public String updateRider(@PathVariable("riderId") Long riderToBeUpdatedId,
                              @RequestParam(value = "bookingId", required = false) Long bookingId,
                              @ModelAttribute("rider") @Valid Rider updatedRider,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            model.addAttribute("riderId", riderToBeUpdatedId);
            model.addAttribute("bookingId", bookingId);
            if (bookingId != null) {
                model.addAttribute("link", riderService.getBookingRiderEquipmentLink(
                        riderService.showOneBookingById(bookingId), riderService.showOneRiderById(riderToBeUpdatedId)));
            }
            return "client/rider/riders";
        }
        riderService.updateRiderById(riderToBeUpdatedId, updatedRider);

        if (bookingId != null) {
            return "redirect:/client/bookings/edit/" + bookingId;
        } else {
            return "redirect:/client/riders";
        }
    }

    // ----- delete -----
    @DeleteMapping("/{riderId}")
    public String deleteRider(@PathVariable("riderId") Long riderId) {
        riderService.deleteRiderById(riderId);
        return "redirect:/client/riders";
    }
}
