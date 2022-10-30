package my.project.skirentalshop.security.applicationUser;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    private String name;
    private String surname;
    private String phone1;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private ApplicationUserRole applicationUserRole;
    private boolean locked = false;
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(applicationUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public ApplicationUser(String name, String surname, String phone1, String email, String password,
                           ApplicationUserRole applicationUserRole) {
        this.name = name;
        this.surname = surname;
        this.phone1 = phone1;
        this.email = email;
        this.password = password;
        this.applicationUserRole = applicationUserRole;
    }
}
