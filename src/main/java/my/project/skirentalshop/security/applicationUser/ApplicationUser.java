package my.project.skirentalshop.security.applicationUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
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
    public String getPassword() {
        return password;
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

    public ApplicationUser() {
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(phone1, that.phone1) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                applicationUserRole == that.applicationUserRole &&
                Objects.equals(locked, that.locked) &&
                Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phone1, email, password, applicationUserRole, locked, enabled);
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + id +
                ", name='" + name +
                ", surname='" + surname +
                ", phone1='" + phone1 +
                ", email='" + email +
                ", password='" + password +
                ", applicationUserRole=" + applicationUserRole +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}
