package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.UpdateBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository,AuthorRepository authorRepository){

        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;

    }

    @Cacheable("books")
    public List<Book> findAll(){
        return bookRepository.findAllWithAuthor();
    }

    @Cacheable(value = "books", key = "#id")
    public Book getBookByID(Long id){
       return  bookRepository.findByIdWithAuthor(id).orElseThrow(
               () -> new BookNotFoundException(id));
    }

    @CacheEvict(value = "books", key = "#id")
    public void delete(Long id){
        bookRepository.delete(
                bookRepository.findById(id).orElseThrow(
                        () -> new BookNotFoundException(id))
        );
    }

    //Method is transactional as its multiple steps,
    //Additionally uses OptimisticLocking on Book as to allow for Concurrency.
    public Book update(Long id, UpdateBookRequestDTO dto) {
        int attempts = 0;

        while (true) {
            try {

                return doUpdate(id, dto);
            } catch (ObjectOptimisticLockingFailureException e) {
                if (++attempts >= 5) {
                    throw e;
                }
            }
        }

    }

    @CachePut(value = "books", key = "#result.id")
    @Transactional//Separated the update attempt due to exception marking transaction to roll back otherwise
    public Book doUpdate(Long id, UpdateBookRequestDTO dto) {

        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));


        updateFields(existing, dto);

        bookRepository.flush();
        return existing;
    }

    @CachePut(value = "books", key="#result.id")
    public Book save(NewBookRequestDTO dto){
        Author author;
        if(dto.author() == null){
            throw new IllegalArgumentException("Author information is required");
        } else {
            if(dto.author().id() != null){
                author = authorRepository.findById(dto.author().id()).orElseThrow(
                        ()-> new AuthorNotFoundException(dto.author().id())
                );
            } else if (dto.author().name() != null){
                author = authorRepository.findByName(dto.author().name()).orElseGet(
                        () -> authorRepository.save(new Author(dto.author().name()))
                );
            } else {
                throw new IllegalArgumentException("ID or name of Author must be supplied");
            }
        }

        Book newBook = Book.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .publishedYear(dto.yearPublished().getValue())
                .author(author)
        .build();

        return bookRepository.save(newBook);
    }


    private void updateFields(Book book, UpdateBookRequestDTO dto){

        if (dto.title() != null) {
            book.setTitle(dto.title());
        }

        if (dto.isbn() != null) {
            book.setIsbn(dto.isbn());
        }

        if (dto.yearPublished() != null) {
            book.setPublishedYear(dto.yearPublished().getValue());
        }

        if (dto.author() != null) {
            if (dto.author().name()==null){
                book.setAuthor(
                        authorRepository.findById(dto.author().id()).orElseThrow(
                                () -> new AuthorNotFoundException("No author with said id"))
                );
            } else {
                book.setAuthor(authorRepository.findByName(dto.author().name()).orElseThrow(()-> new AuthorNotFoundException("No author by that name")));
            }
        }
    }
}

