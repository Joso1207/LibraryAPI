package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.api.v1.mapper.Mapper;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
               () -> new BookNotFoundException(id));
       return mapper.toBookDTO(entity);
    }

    public BookRequestDTO save(NewBookRequestDTO dto){
        Book newBook = mapper.dtoToBook(dto);
        return mapper.toBookDTO(repo.save(newBook));
    }


}
