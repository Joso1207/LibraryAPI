package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Book getBookByID(Long id){
       return  bookRepository.findById(id).orElseThrow(
               () -> new BookNotFoundException(id));
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
                throw new IllegalArgumentException("ID or name must be supplied");
            }
        }

        Book newBook = Book.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .publishedYear(dto.yearPublished())
                .author(author)
        .build();

        return bookRepository.save(newBook);
    }


}
