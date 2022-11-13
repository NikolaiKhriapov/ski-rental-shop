package my.project.skirentalshop.security;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import my.project.skirentalshop.security.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final RegistrationService registrationService;

    @Autowired
    public AuthenticationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "authentication/sign_in";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "authentication/sign_up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("registrationRequest") @Valid RegistrationRequest registrationRequest,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authentication/sign_up";
        }
        registrationService.register(registrationRequest);
        return "redirect:/sign-in";
    }

    @RequestMapping("/home")
    public String defaultAfterLogin() {
        ApplicationUser applicationUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUserRole applicationUserRole = applicationUser.getApplicationUserRole();
        return "redirect:/" + applicationUserRole;
    }
}
