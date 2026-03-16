package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.mapper.BookMapper;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/books")
public class BookController {

    private final BookService service;
    private final BookMapperV1 mapper;

    public BookController(BookService service, BookMapperV1 mapper,BookMapper coreMapper){
        this.service = service;
        this.mapper = mapper;
    }


    @PostMapping
    public ResponseEntity<BookRequestDTOv1> addBook(@RequestBody NewBookRequestDTOv1 postRequest){
        NewBookRequestDTO newBook = mapper.v1ToCoreDTO(postRequest);
        BookRequestDTOv1 responseBody = mapper.coreToV1DTO(service.save(newBook));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    public List<BookRequestDTOv1> getAll(){
        return service.findAll().stream().map(mapper::coreToV1DTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTOv1> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(mapper.coreToV1DTO(service.getBookByID(id)));
    }



}
