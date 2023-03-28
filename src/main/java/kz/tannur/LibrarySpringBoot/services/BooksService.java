package kz.tannur.LibrarySpringBoot.services;

import kz.tannur.LibrarySpringBoot.models.Book;
import kz.tannur.LibrarySpringBoot.models.Person;
import kz.tannur.LibrarySpringBoot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAllBooks(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOneBook(int id){
        Optional<Book> foundBook =  booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void saveBook(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void deleteBook(int id){
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {

        return booksRepository.findById(id).map(Book::getOwner).orElse(null);

        //return jdbcTemplate.query("SELECT person.* FROM person JOIN book ON person.id=book.person_id WHERE book.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
        //        .stream().findAny();
    }

    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                });

       // jdbcTemplate.update("UPDATE book SET person_id=NULL WHERE id=?", id);
    }

    @Transactional
    public void assign(int id, Person selectedPerson){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date()); // текущее время
                }
        );

        //jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }

    public List<Book> searchByTitle(String query) {
        return booksRepository.findByNameStartingWith(query);
    }
}
