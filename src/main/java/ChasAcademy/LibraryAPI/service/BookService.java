package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.BookCreationCMD;
import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.api.core.mapper.BookMapper;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;
    private final BookMapper mapper;

    @Autowired
    public BookService(BookRepository repo, BookMapper mapper){

        this.repo = repo;
        this.mapper = mapper;

    }

    public List<BookRequestDTO> findAll(){
        return repo.findAll()
                .stream()
                .map(mapper::toBookDTO)
                .toList();
    }

    public @NonNull BookRequestDTO getBookByID(Long id){
       Book entity = repo.findById(id).orElseThrow(
               () -> new BookNotFoundException(id));
       return mapper.toBookDTO(entity);
    }

    public BookRequestDTO save(NewBookRequestDTO newbook){
        Book newBook = mapper.dtoToBook(newbook);
        return mapper.toBookDTO(repo.save(newBook));
    }


}
