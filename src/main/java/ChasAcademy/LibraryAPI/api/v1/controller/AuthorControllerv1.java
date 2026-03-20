package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.AuthorMapperV1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/api/authors")
public class AuthorControllerv1 {

    private final AuthorMapperV1 mapper;
    private final AuthorService service;
    private final BookMapperV1 bookMapper;

    public AuthorControllerv1(AuthorMapperV1 mapper, AuthorService service, BookMapperV1 bookMapper){
        this.mapper = mapper;
        this.service = service;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public List<AuthorDTO> getAll(){
         return service.findAll().stream().map(mapper::authorToDTO).toList();
    }

    @GetMapping("/{ID}")
    public ResponseEntity<AuthorDTO> getAuthorByID(@PathVariable Long ID){
        return ResponseEntity.ok(mapper.authorToDTO(service.findAuthorByID(ID)));
    }

    @GetMapping("/{authorID}/books")
    public ResponseEntity<List<BookRequestDTOv1>> getWrittenWorks(@PathVariable Long authorID){
        return ResponseEntity.ok(
                service.findAuthorByID(authorID)
                        .getWrittenWorks()
                        .stream()
                        .map(bookMapper::bookToDTOV1)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody NewAuthorDTO postRequest){
        AuthorDTO entity = mapper.authorToDTO(service.addAuthor(postRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }



}
