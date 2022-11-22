package my.project.skirentalshop.controller.admin;

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

    @GetMapping
    public String showAdminHome(Model model) {
        model.addAttribute("allBookings", bookingService.showAllIncompleteBookings());
        model.addAttribute("currentBookings", bookingService.showAllCurrentBookings());
        return "admin/admin_home";
    }

    @GetMapping("/bookings-today")
    public String showBookingsForToday(Model model) {
        Date todayBeginning = bookingService.getTodayBeginningAndEnd()[0];
        Date todayEnd = bookingService.getTodayBeginningAndEnd()[1];

        model.addAttribute("date", todayBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(todayBeginning, todayEnd));
        return "admin/bookings_by_date";
    }

    @GetMapping("/bookings-tomorrow")
    public String showBookingsForTomorrow(Model model) {
        Date tomorrowBeginning = bookingService.getTomorrowBeginningAndEnd()[0];
        Date tomorrowEnd = bookingService.getTomorrowBeginningAndEnd()[1];

        model.addAttribute("date", tomorrowBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(tomorrowBeginning, tomorrowEnd));
        return "admin/bookings_by_date";
    }

    @GetMapping("/{bookingId}/change-completed")
    public String changeBookingCompleted(@PathVariable("bookingId") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/admin";
    }
}
