package my.project.skirentalshop.controller;

import my.project.skirentalshop.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private final BookingService bookingService;

    @Autowired
    public AdminHomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // admin home page
    @GetMapping()
    public String showAdminMainPage(Model model) {
        model.addAttribute("allBookings", bookingService.showAllIncompleteBookings());
        return "admin/main_page";
    }

    // hyperlink to equipment
    @GetMapping("/info-equipment")
    public String showEquipmentChoicePage() {
        return "admin/equipment_info";
    }

    // ----- bookings for today -----
    @GetMapping("/show-today")
    public String showBookingsForToday(Model model) {
        Date todayBeginning = bookingService.getTodayBeginningAndEnd()[0];
        Date todayEnd = bookingService.getTodayBeginningAndEnd()[1];

        model.addAttribute("date", todayBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(todayBeginning, todayEnd));
        return "admin/bookings_by_date";
    }

    // ----- bookings for tomorrow -----
    @GetMapping("/show-tomorrow")
    public String showBookingsForTomorrow(Model model) {
        Date tomorrowBeginning = bookingService.getTomorrowBeginningAndEnd()[0];
        Date tomorrowEnd = bookingService.getTomorrowBeginningAndEnd()[1];

        model.addAttribute("date", tomorrowBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(tomorrowBeginning, tomorrowEnd));
        return "admin/bookings_by_date";
    }

    // ----- mark booking completed -----
    @GetMapping("/change-booking-completed/{id}")
    public String changeBookingCompleted(@PathVariable("id") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/admin";
    }

    // ----- delete booking -----
    @DeleteMapping("/delete-booking/{id}")
    public String deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBookingById(id);
        return "redirect:/admin";
    }
}
