package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author extends BaseEntity {

    private String firstName;
    private String lastName;
    private String birthTown;
    private int age;
    private Set<Book> books;

    public Author(int age, String firstName, String lastName, String birthTown) {
        books = new HashSet<>();
        setAge(age);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthTown(birthTown);
    }

    public Author() {

    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Column(name = "birth_town", nullable = false)
    public String getBirthTown() {
        return birthTown;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthTown(String birthTown) {
        this.birthTown = birthTown;
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(firstName).append(" ").append(lastName).append(" from ").append(birthTown);
//        return sb.toString();
//    }
}
