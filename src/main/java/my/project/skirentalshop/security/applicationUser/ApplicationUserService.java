package my.project.skirentalshop.security.applicationUser;

import my.project.skirentalshop.security.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static my.project.skirentalshop.security.applicationUser.ApplicationUserRole.*;

@Service
public class ApplicationUserService implements UserDetailsService {

    private static final String DIRECTORY_FOR_USER_PHOTOS = "src/main/resources/static/images/user-photos/";

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
        return applicationUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                getExceptionMessage("exception.app-user.email-not-found", email)
        ));
    }

    public void signUpUser(ApplicationUser applicationUser) {
        boolean userExists = checkIfExists(applicationUser.getEmail());
        if (userExists) {
            throw new IllegalStateException(
                    getExceptionMessage("exception.app-user.email-taken", applicationUser.getEmail())
            );
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

    public void updatePhoto(ApplicationUser applicationUser, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = applicationUser.getId() + "-user-photo" + Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            if (applicationUser.getPhoto() != null) {
                Path oldFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, applicationUser.getPhoto());
                Files.delete(oldFileNameAndPath);
            }

            Path newFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, fileName);
            Files.write(newFileNameAndPath, file.getBytes());

            applicationUser.setPhoto(fileName);
            applicationUserRepository.save(applicationUser);
        }
    }

    public void deletePhoto(ApplicationUser applicationUser) throws IOException {
        if (applicationUser.getPhoto() != null) {

            Path oldFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, applicationUser.getPhoto());
            Files.delete(oldFileNameAndPath);

            applicationUser.setPhoto(null);
        }
    }

    public List<ApplicationUser> showAllApplicationUsers() {
        return applicationUserRepository.findAllByApplicationUserRoleNot(ADMIN);
    }

    public void changeApplicationUserLocked(Long applicationUserId) {
        ApplicationUser user = applicationUserRepository.findById(applicationUserId).orElseThrow(() ->
                new IllegalStateException(getExceptionMessage(
                        "exception.app-user.id-not-found", applicationUserId.toString())
                )
        );
        user.setLocked(!user.isLocked());
        applicationUserRepository.save(user);
    }

    // ----- search -----
    public List<ApplicationUser> showApplicationUsersBySearch(String search) {
        List<ApplicationUser> listOfApplicationUsers = applicationUserRepository
                .findAllByClientNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
        listOfApplicationUsers.removeIf(oneUser -> oneUser.getApplicationUserRole() == ADMIN);
        return listOfApplicationUsers;
    }

    // ----- sort -----
    public List<ApplicationUser> sortAllApplicationUsersByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return applicationUserRepository.findAll(sort);
    }

    // ----- supplementary -----
    public String getExceptionMessage(String propertyKey, String parameter) {
        return String.format(
                ResourceBundle
                        .getBundle("exception", LocaleContextHolder.getLocale())
                        .getString(propertyKey),
                parameter
        );
    }
}
