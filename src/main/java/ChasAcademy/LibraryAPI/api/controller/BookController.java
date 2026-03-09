package ChasAcademy.LibraryAPI.api.controller;

import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service){
        this.service=service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTO> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(service.getBookByID(id));
    }


}
