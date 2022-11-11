package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/application-users")
public class AdminSettingsController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public AdminSettingsController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    // ----- show all -----
    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfApplicationUsers", applicationUserService.showAllApplicationUsers());
        return "authentication/application_user";
    }

    // ----- lock one -----
    @GetMapping("/{applicationUserId}")
    public String changeLocked(@PathVariable("applicationUserId") Long applicationUserId) {
        applicationUserService.changeApplicationUserLocked(applicationUserId);
        return "redirect:/admin/application-users";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllBySearch(@RequestParam("search") String search,
                                  Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfApplicationUsers",
                applicationUserService.showApplicationUsersBySearch(search));
        model.addAttribute("search", search);
        return "authentication/application_user";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllByParameter(@RequestParam("parameter") String parameter,
                                     @RequestParam("sortDirection") String sortDirection,
                                     Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfApplicationUsers",
                applicationUserService.sortAllApplicationUsersByParameter(parameter, sortDirection));
        return "authentication/application_user";
    }
}
