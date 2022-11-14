package my.project.skirentalshop.security.applicationUser;

import lombok.*;
import my.project.skirentalshop.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@Data
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole applicationUserRole;

    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String email;

    private String password;

    private boolean locked = false;

    private boolean enabled = true;

    private String photo;

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

    public ApplicationUser(Client client, String email, String password, ApplicationUserRole applicationUserRole) {
        this.client = client;
        this.email = email;
        this.password = password;
        this.applicationUserRole = applicationUserRole;
    }
}
