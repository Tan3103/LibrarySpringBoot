package kz.tannur.LibrarySpringBoot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 2, max = 100, message = "Full name should be more 2 and less 100 symbols")
    @Column(name = "fullname")
    private String fullName;

    @Min(value = 1900, message = "Year of birth should be more 1900")
    @Column(name = "year_of_birth")
    private int year_of_birth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(){
    }

    public Person(String fullName, int year_of_birth) {
        this.fullName = fullName;
        this.year_of_birth = year_of_birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
