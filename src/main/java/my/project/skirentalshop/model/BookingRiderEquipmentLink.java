package my.project.skirentalshop.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class BookingRiderEquipmentLink {
    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Booking booking;

    @ManyToOne
    @JoinColumn
    private Rider rider;

    @ElementCollection(targetClass = TypesOfEquipment.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn)
    private List<TypesOfEquipment> riderRequestedEquipment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private RiderAssignedEquipment riderAssignedEquipment;

    public BookingRiderEquipmentLink() {
    }

    public BookingRiderEquipmentLink(Booking booking,
                                     Rider rider,
                                     List<TypesOfEquipment> riderRequestedEquipment,
                                     RiderAssignedEquipment riderAssignedEquipment) {
        this.booking = booking;
        this.rider = rider;
        this.riderRequestedEquipment = riderRequestedEquipment;
        this.riderAssignedEquipment = riderAssignedEquipment;
    }

    public Long getId() {
        return id;
    }

    public Booking getBooking() {
        if (booking == null) {
            return new Booking();
        }
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Rider getRider() {
        if (rider == null) {
            return new Rider();
        }
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public List<TypesOfEquipment> getRiderRequestedEquipment() {
        if (riderRequestedEquipment == null) {
            return new ArrayList<>();
        }
        return riderRequestedEquipment;
    }

    public void setRiderRequestedEquipment(List<TypesOfEquipment> riderRequestedEquipment) {
        this.riderRequestedEquipment = riderRequestedEquipment;
    }

    public RiderAssignedEquipment getRiderAssignedEquipment() {
        if (riderAssignedEquipment == null) {
            return new RiderAssignedEquipment();
        }
        return riderAssignedEquipment;
    }

    public void setRiderAssignedEquipment(RiderAssignedEquipment riderAssignedEquipment) {
        this.riderAssignedEquipment = riderAssignedEquipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRiderEquipmentLink that = (BookingRiderEquipmentLink) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(booking, that.booking) &&
                Objects.equals(rider, that.rider) &&
                Objects.equals(riderRequestedEquipment, that.riderRequestedEquipment) &&
                Objects.equals(riderAssignedEquipment, that.riderAssignedEquipment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, booking, rider, riderRequestedEquipment, riderAssignedEquipment);
    }

    @Override
    public String toString() { //TODO: check toString()
        return "BookingRiderEquipmentLink{" +
                "id=" + id +
                ", booking=" + booking +
                ", rider=" + rider +
                ", riderRequestedEquipment=" + riderRequestedEquipment +
                ", riderAssignedEquipment=" + riderAssignedEquipment +
                '}';
    }
}
