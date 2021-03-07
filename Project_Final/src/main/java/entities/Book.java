package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    private String name;
    private int pages;
    private String genre;
    private Author author;
    private Set<Library> libraries;

    public Book() {
        libraries = new HashSet<>();
    }

    public Book (String name, int edition, String genre) {
        this.name = name;
        this.pages = edition;
        this.genre = genre;
        libraries = new HashSet<>();
    }

    @Column(name = "name", nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "pages", nullable = true)
    public int getPages() {
        return pages;
    }

    public void setPages(int edition) {
        this.pages = edition;
    }

    @Column(name = "genre", nullable = true)
    public String getGenre() {
        return genre;
    }

    public void setGenre(String description) {
        this.genre = description;
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @ManyToMany
    @JoinTable(name = "books_libraries",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id"))
    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    public void addLibrary(Library library) {
        if (this.libraries.size() == 0) {
            this.libraries = new HashSet<>();
        }
        this.libraries.add(library);
    }
}
