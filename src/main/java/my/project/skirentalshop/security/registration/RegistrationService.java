package my.project.skirentalshop.security.registration;

import my.project.skirentalshop.security.applicationUser.ApplicationUser;
import my.project.skirentalshop.security.applicationUser.ApplicationUserRole;
import my.project.skirentalshop.security.applicationUser.ApplicationUserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final ApplicationUserService applicationUserService;

    public RegistrationService(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    public void register(RegistrationRequest registrationRequest) {
        applicationUserService.signUpUser(new ApplicationUser(
                registrationRequest.getName(),
                registrationRequest.getSurname(),
                registrationRequest.getPhone1(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                ApplicationUserRole.CLIENT
        ));
    }
}
