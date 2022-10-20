package my.project.skirentalshop.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Client {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;
    @NotEmpty(message = "{validation.client.invalid_name}")
    private String name;
    @Pattern(regexp = "[\\d]\\([\\d]{3}\\)[\\d]{3}-[\\d]{2}-[\\d]{2}", message = "{validation.client.invalid_phone_number}")
    private String phone1;
    private String phone2;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.client.invalid_email}")
    private String email;
    @OneToMany(mappedBy = "client")
    private List<Booking> listOfBookings;

    public Client() {
    }

    public Client(String name, String phone1, String phone2) {
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name) &&
                Objects.equals(phone1, client.phone1) && Objects.equals(phone2, client.phone2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone1, phone2);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name +
                ", phone1='" + phone1 +
                ", phone2='" + phone2 +
                '}';
    }
}
