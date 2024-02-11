package at.technikum.drivingschool.bookingappbackend.models;
import jakarta.persistence.*;

/**
 * Address of a user
 */
@Entity
@Table(name = "user_addr_tbl")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String street;
    private String nr;
    private String plz;
    private String city;
    private String country;

    public Address() {
    }
    public Address(String street, String nr, String plz, String city, String country) {
        this.street = street;
        this.nr = nr;
        this.plz = plz;
        this.city = city;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
