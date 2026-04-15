package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.AuthorMapperV1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/api/authors")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "BAD REQUEST",
                content = @Content(
                        schema = @Schema(implementation = ApiError.class)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "CONFLICT",
                content = @Content(
                        schema = @Schema(implementation = ApiError.class)
                )
        )
})
public class AuthorControllerv1 {

    private final AuthorMapperV1 mapper;
    private final AuthorService service;
    private final BookMapperV1 bookMapper;

    public AuthorControllerv1(AuthorMapperV1 mapper, AuthorService service, BookMapperV1 bookMapper){
        this.mapper = mapper;
        this.service = service;
        this.bookMapper = bookMapper;
    }

    @Operation(summary = "Get all authors")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<AuthorDTO> getAll(){
         return service.findAll().stream().map(mapper::authorToDTO).toList();
    }

    @Operation(summary = "Get specific author")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
    })
    @GetMapping("/{ID}")
    public ResponseEntity<AuthorDTO> getAuthorByID(@PathVariable Long ID){
        return ResponseEntity.ok(mapper.authorToDTO(service.findAuthorByID(ID)));
    }

    @Operation(summary = "Get author's written works",
            description = "Returns a list of all books attributed to author")
    @ApiResponse(responseCode = "200", description = "Success")
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

    @Operation(summary = "Create author")
    @ApiResponse(responseCode = "201", description = "Author created successfully")
    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@Valid @RequestBody NewAuthorDTO postRequest){
        AuthorDTO entity = mapper.authorToDTO(service.addAuthor(postRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }



}
