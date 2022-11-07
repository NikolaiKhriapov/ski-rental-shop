package my.project.skirentalshop.security.applicationUser;

import my.project.skirentalshop.security.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MESSAGE = "User with email %s not found!";

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
                                  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return applicationUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    public void signUpUser(ApplicationUser applicationUser) {
        boolean userExists = checkIfExists(applicationUser.getEmail());
        if (userExists) {
            throw new IllegalStateException("Email already taken!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encodedPassword);

        applicationUserRepository.save(applicationUser);
    }

    public boolean checkIfExists(String email) {
        return applicationUserRepository.findByEmail(email).isPresent();
    }

    public void updatePersonalInfo(ApplicationUser applicationUserToBeUpdated,
                                   RegistrationRequest registrationRequest) {

        boolean emailExists = checkIfExists(registrationRequest.getEmail());
        if (!emailExists || registrationRequest.getEmail().equals(applicationUserToBeUpdated.getEmail())) {
            applicationUserToBeUpdated.getClient().setName(registrationRequest.getName());
            applicationUserToBeUpdated.getClient().setPhone1(registrationRequest.getPhone1());
            applicationUserToBeUpdated.setEmail(registrationRequest.getEmail());

            applicationUserRepository.save(applicationUserToBeUpdated);
        }
    }

    public void updatePassword(ApplicationUser applicationUserToBeUpdated, String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        applicationUserToBeUpdated.setPassword(encodedPassword);

        applicationUserRepository.save(applicationUserToBeUpdated);
    }
}
