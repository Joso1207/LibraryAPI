package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.service.BookService;
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
@RequestMapping("/v1/api/books")
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
public class BookControllerv1 {

    private final BookService service;
    private final BookMapperV1 mapper;

    public BookControllerv1(BookService service, BookMapperV1 mapper){
        this.service = service;
        this.mapper = mapper;
    }


    @Operation(summary = "Create new Book")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @PostMapping
    public ResponseEntity<BookRequestDTOv1> addBook(@Valid @RequestBody NewBookRequestDTOv1 postRequest){
        NewBookRequestDTO newBook = mapper.v1dtoToBookRequest(postRequest);
        BookRequestDTOv1 responseBody = mapper.bookToDTOV1(service.save(newBook));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<BookRequestDTOv1> getAll(){
        return service.findAll().stream().map(mapper::bookToDTOV1).toList();
    }

    @Operation(summary = "Get specific book")
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
    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTOv1> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(mapper.bookToDTOV1(service.getBookByID(id)));
    }



}
