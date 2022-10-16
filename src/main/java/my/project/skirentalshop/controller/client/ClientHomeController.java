package my.project.skirentalshop.controller.client;

import my.project.skirentalshop.service.BookingService;
import my.project.skirentalshop.service.ClientService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientHomeController {

    private final BookingService bookingService;
    private final ClientService clientService;

    public ClientHomeController(BookingService bookingService,
                                ClientService clientService) {
        this.bookingService = bookingService;
        this.clientService = clientService;
    }

    // ----- client home page -----
    @GetMapping()
    public String showClientMainPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentBookingsForClient", bookingService.showCurrentBookingsForClient(username));
        return "client/home/main_page";
    }

    // ----- client history -----
    @GetMapping("/history")
    public String showClientHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("allBookingsForClient", bookingService.showAllBookingsForClient(username));
        return "client/home/history";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentClient", clientService.showOneClientByEmail(username));
    }
}
