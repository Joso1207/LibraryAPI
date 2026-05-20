package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.UpdateBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v1.dto.BookResponseDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<BookResponseDTOv1> addBook(@Valid @RequestBody NewBookRequestDTOv1 postRequest){
        NewBookRequestDTO newBook = mapper.v1dtoToBookRequest(postRequest);
        BookResponseDTOv1 responseBody = mapper.bookToDTOV1(service.save(newBook));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @Operation(summary = "Get a list of books")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public Page<BookResponseDTOv1> getBooks(Pageable pageable){
        return service.findAll(pageable).map(mapper::bookToDTOV1);
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
    public ResponseEntity<BookResponseDTOv1> getBookByID(@PathVariable Long id){
        return ResponseEntity.ok(mapper.bookToDTOV1(service.getBookByID(id)));
    }

    @Operation(summary = "Update the book with ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                    schema = @Schema(implementation = ApiError.class)
            )
    )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTOv1> update(
            @PathVariable Long id,
            @RequestBody UpdateBookRequestDTO dto
    ) {
        Book updated = service.update(id, dto);
        return ResponseEntity.ok(mapper.bookToDTOV1(updated));
    }

    @Operation(summary = "Delete a book")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted entry"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
