package ChasAcademy.LibraryAPI.api.v2.controller;

import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.v2.dto.NewBookRequestDTOv2;
import ChasAcademy.LibraryAPI.api.v2.mapper.BookMapperV2;
import ChasAcademy.LibraryAPI.api.v2.dto.BookRequestDTOv2;
import ChasAcademy.LibraryAPI.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/api/books")
public class BookControllerv2 {

    private final BookService service;
    private final BookMapperV2 mapper;

    public BookControllerv2(BookService service, BookMapperV2 mapper){
        this.service = service;
        this.mapper = mapper;
    }


    @PostMapping
    public ResponseEntity<BookRequestDTOv2> addBook(@RequestBody NewBookRequestDTOv2 postRequest){
        NewBookRequestDTO newBook = mapper.dtoToBook(postRequest);
        BookRequestDTOv2 responseBody = mapper.toBookDTO(service.save(newBook));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    public List<BookRequestDTOv2> getAll(){
        return service.findAll().stream().map(mapper::toBookDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTOv2> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toBookDTO(service.getBookByID(id)));
    }



}
