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

    // ----- admin home page -----
    @GetMapping()
    public String showAdminHome(Model model) {
        model.addAttribute("allBookings", bookingService.showAllIncompleteBookings());
        model.addAttribute("currentBookings", bookingService.showAllCurrentBookings());
        return "admin/home/admin_home";
    }

    // ----- bookings for today -----
    @GetMapping("/show-today")
    public String showBookingsForToday(Model model) {
        Date todayBeginning = bookingService.getTodayBeginningAndEnd()[0];
        Date todayEnd = bookingService.getTodayBeginningAndEnd()[1];

        model.addAttribute("date", todayBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(todayBeginning, todayEnd));
        model.addAttribute("day", "today");
        return "admin/home/bookings_by_date";
    }

    // ----- bookings for tomorrow -----
    @GetMapping("/show-tomorrow")
    public String showBookingsForTomorrow(Model model) {
        Date tomorrowBeginning = bookingService.getTomorrowBeginningAndEnd()[0];
        Date tomorrowEnd = bookingService.getTomorrowBeginningAndEnd()[1];

        model.addAttribute("date", tomorrowBeginning);
        model.addAttribute("bookingsForTheDate", bookingService.showBookingsForTheDate(tomorrowBeginning, tomorrowEnd));
        model.addAttribute("day", "tomorrow");
        return "admin/home/bookings_by_date";
    }

    // ----- mark booking completed -----
    @GetMapping("/change-completed/{bookingId}")
    public String changeBookingCompleted(@PathVariable("bookingId") Long bookingId) {
        bookingService.changeBookingCompleted(bookingId);
        return "redirect:/admin";
    }
}
