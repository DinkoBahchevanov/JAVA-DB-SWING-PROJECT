package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libraries")
public class Library extends BaseEntity {

    private String name;
    private String location;
    private int rating;
    private Set<Book> books;

    public Library() {

    }

    public Library(String name, String location, int rating) {
        books = new HashSet<>();
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @ManyToMany(mappedBy = "libraries")
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> book) {
        this.books = book;
    }
}
