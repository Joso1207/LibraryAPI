package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.mapper.Mapper;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;
    private final Mapper mapper;

    @Autowired
    public BookService(BookRepository repo, Mapper mapper){

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
               () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
       return mapper.toBookDTO(entity);
    }

    public BookRequestDTO save(NewBookRequestDTO dto){
        Book newBook = mapper.dtoToBook(dto);
        return mapper.toBookDTO(repo.save(newBook));
    }


}
