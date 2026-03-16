package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.api.core.mapper.BookMapper;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper mapper,AuthorRepository authorRepository){

        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.authorRepository = authorRepository;

    }

    public List<BookRequestDTO> findAll(){
        return bookRepository.findAll()
                .stream()
                .map(mapper::toBookDTO)
                .toList();
    }

    public @NonNull BookRequestDTO getBookByID(Long id){
       Book entity = bookRepository.findById(id).orElseThrow(
               () -> new BookNotFoundException(id));
       return mapper.toBookDTO(entity);
    }

    public BookRequestDTO save(NewBookRequestDTO dto){
        Author author;
        if(dto.author() == null){
            throw new IllegalArgumentException("Author information is required");
        } else {
            if(dto.author().id() != null){
                author = authorRepository.findById(dto.author().id()).orElseThrow(
                        ()-> new AuthorNotFoundException(dto.author().id())
                );
            } else if (dto.author().name() != null){
                author = authorRepository.findByName(dto.author().name()).orElseThrow(
                        ()-> new AuthorNotFoundException(dto.author().name())
                );
            } else {
                throw new IllegalArgumentException("ID or name must be supplied");
            }
        }

        Book newBook = mapper.dtoToBook(dto);
        newBook.setAuthor(author);
        return mapper.toBookDTO(bookRepository.save(newBook));
    }


}
