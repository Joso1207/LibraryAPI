package ChasAcademy.LibraryAPI.api.controller;

import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service){
        this.service=service;
    }


    @PostMapping
    public ResponseEntity<BookRequestDTO> addBook(@RequestBody NewBookRequestDTO newBook){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(newBook));
    }

    @GetMapping
    public List<BookRequestDTO> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTO> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(service.getBookByID(id));
    }



}
