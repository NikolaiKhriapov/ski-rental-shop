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
@RequestMapping("/client/info-riders")
public class ClientRiderController {

    private final RiderService riderService;

    @Autowired
    public ClientRiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllRidersForClient(Model model) {
        model.addAttribute("allRidersForClient", riderService.showAllRidersForCurrentClient());
        return "client/rider/show_all";
    }

    // ----- add new rider -----
    @GetMapping("/add-new")
    public String createNewRider(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                 Model model) {
        model.addAttribute("newRider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return "client/rider/add_new";
    }

    @PostMapping("/add-new")
    public String addNewRiderToDb(@RequestParam(value = "bookingId", required = false) Long bookingId,
                                  @ModelAttribute("newRider") @Valid Rider rider,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/rider/add_new";
        }
        riderService.addNewRiderToDB(rider);

        if (bookingId != null) {
            riderService.addRiderToBooking(bookingId, rider);
            return "redirect:/client/info-riders/add-new?bookingId=" + bookingId;
        }
        return "redirect:/client/info-riders/";
    }

    // ----- edit -----
    @GetMapping("/edit")
    public String showOneRider(@RequestParam("riderId") Long riderId,
                               @RequestParam(value = "bookingId", required = false) Long bookingId,
                               Model model) {
        model.addAttribute("riderToUpdate", riderService.showOneRiderById(riderId));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", riderId);
        if (bookingId != null) {
            model.addAttribute("link", riderService.getBookingRiderEquipmentLink(
                    riderService.showOneBookingById(bookingId), riderService.showOneRiderById(riderId)));
        }
        return "client/rider/edit";
    }

    @PatchMapping("/edit/{riderId}")
    public String updateRider(@PathVariable("riderId") Long riderToBeUpdatedId,
                              @RequestParam(value = "bookingId", required = false) Long bookingId,
                              @ModelAttribute("riderToUpdate") @Valid Rider updatedRider,
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
    @DeleteMapping("/")
    public String deleteRider(@RequestParam("riderId") Long riderId) {
        riderService.deleteRiderById(riderId);
        return "redirect:/client/info-riders";
    }
}
