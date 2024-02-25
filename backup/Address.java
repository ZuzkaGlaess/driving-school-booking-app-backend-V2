package at.technikum.drivingschool.bookingappbackend.model;
import jakarta.persistence.*;

/**
 * Address of a user
 */
@Entity
@Table(name = "user_addr_tbl")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String street;
    private String nr;
    private String plz;
    private String city;
    private String country;

    /**
     * Address empty constructor
     */
    public Address() {
    }

    /**
     * Address constructor
     * @param street
     * @param nr
     * @param plz
     * @param city
     * @param country
     */
    public Address(String street, String nr, String plz, String city, String country) {
        this.street = street;
        this.nr = nr;
        this.plz = plz;
        this.city = city;
        this.country = country;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return street information
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street
     */
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
