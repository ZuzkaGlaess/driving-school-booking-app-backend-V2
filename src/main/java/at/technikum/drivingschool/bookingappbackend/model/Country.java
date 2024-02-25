package at.technikum.drivingschool.bookingappbackend.model;

import jakarta.persistence.*;
/**
 * Country of a user
 */
@Entity
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    /**
     * Country empty constructor
     */
    public Country() {
    }

    /**
     * Constructor for country
     * @param name
     */
    public Country(String name) {
        this.name = name;
    }

    /**
     * Unique table key
     * @return id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

