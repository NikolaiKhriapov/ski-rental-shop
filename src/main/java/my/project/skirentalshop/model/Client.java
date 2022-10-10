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
    @NotEmpty(message = "{validation.client.invalid_surname}")
    private String surname;
    @Pattern(regexp = "[\\d]\\([\\d]{3}\\)[\\d]{3}-[\\d]{2}-[\\d]{2}", message = "{validation.client.invalid_phone_number}")
    private String phone1;
    private String phone2;
    @OneToMany(mappedBy = "client")
    private List<Booking> listOfBookings;

    public Client() {
    }

    public Client(String surname, String phone1, String phone2) {
        this.surname = surname;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public Long getId() {
        return id;
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

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(surname, client.surname) &&
                Objects.equals(phone1, client.phone1) && Objects.equals(phone2, client.phone2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, phone1, phone2);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", surname='" + surname +
                ", phone1='" + phone1 +
                ", phone2='" + phone2 +
                '}';
    }
}
