package ChasAcademy.LibraryAPI.api.controller;

import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/books")
public class BookControllerV1 {

    private final BookService service;

    public BookControllerV1(BookService service){
        this.service=service;
    }


    @PostMapping
    public BookRequestDTO addBook(@RequestBody NewBookRequestDTO newBook){
        return service.save(newBook);
    }

    @GetMapping
    public List<BookRequestDTO> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public BookRequestDTO getBookByID(@PathVariable Long id){
        return service.getBookByID(id);
    }



}
