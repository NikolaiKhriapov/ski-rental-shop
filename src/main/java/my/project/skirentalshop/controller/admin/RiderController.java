package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Rider;
import my.project.skirentalshop.service.BookingService;
import my.project.skirentalshop.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-riders")
public class RiderController {

    private final BookingService bookingService;
    private final RiderService riderService;

    @Autowired
    public RiderController(RiderService riderService, BookingService bookingService) {
        this.riderService = riderService;
        this.bookingService = bookingService;
    }

    //show all
    @GetMapping()
    public String showAllRiders(Model model) {
        model.addAttribute("allRiders", riderService.showAllRiders());
        model.addAttribute("allBookings", bookingService.showAllBookings());
        return "admin/rider/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewRider(Model model, @RequestParam(value = "id", required = false) Long bookingId) {
        model.addAttribute("newRider", new Rider());
        model.addAttribute("bookingId", bookingId);
        return "admin/rider/add_new";
    }

    @PostMapping("/add")
    public String addNewRiderToDb(@RequestParam(value = "id", required = false) Long bookingId,
                                  @ModelAttribute("newRider") @Valid Rider rider,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/rider/add_new";
        }
        riderService.addNewRiderToDB(rider);

        if (bookingId != null) {
            bookingService.addNewRiderToBooking(bookingId, rider);
            return "redirect:/admin/info-riders/add-new?id=" + bookingId;
        }
        return "redirect:/admin/info-riders/";
    }

    // ----- edit -----
    @GetMapping("/edit")
    public String showOneRider(@RequestParam("id") Long id,
                               @RequestParam(value = "bookingId", required = false) Long bookingId,
                               Model model) {
        model.addAttribute("riderToUpdate", riderService.showOneRiderById(id));
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("riderId", id);
        return "admin/rider/edit";
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
            return "admin/rider/edit";
        }
        riderService.updateRiderById(riderToBeUpdatedId, updatedRider);
        if (bookingId != null) {
            return "redirect:/admin/info-booking/edit/" + bookingId;
        } else {
            return "redirect:/admin/info-riders";
        }
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteRider(@PathVariable("id") Long id) {
        riderService.deleteRiderById(id);
        return "redirect:/admin/info-riders";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllRidersByParameter(@RequestParam("parameter") String parameter,
                                           @RequestParam("sortDirection") String sortDirection,
                                           Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allRiders", riderService.sortAllRidersByParameter(parameter, sortDirection));
        model.addAttribute("allBookings", bookingService.showAllBookings());
        return "admin/rider/show_all";
    }
}
