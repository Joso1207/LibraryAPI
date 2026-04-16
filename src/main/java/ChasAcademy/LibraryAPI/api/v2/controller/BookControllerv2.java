package ChasAcademy.LibraryAPI.api.v2.controller;

import ChasAcademy.LibraryAPI.api.core.ApiResponseWrapper;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.UpdateBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v2.dto.BookResponseDTOv2;
import ChasAcademy.LibraryAPI.api.v2.mapper.BookMapperV2;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.service.BookService;
import ChasAcademy.LibraryAPI.service.LoanService;
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
@RequestMapping("/v2/api/books")
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
public class BookControllerv2 {

    private final BookService service;
    private final LoanService loanService;
    private final BookMapperV2 mapper;

    public BookControllerv2(BookService service, BookMapperV2 mapper,LoanService loanService){
        this.service = service;
        this.loanService = loanService;
        this.mapper = mapper;
    }


    @Operation(summary = "Create new Book")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<BookResponseDTOv2>> addBook(@Valid @RequestBody NewBookRequestDTO postRequest){
        NewBookRequestDTO newBook = mapper.v2dtoToBookRequest(postRequest);
        BookResponseDTOv2 responseBody = mapper.bookToDTOV2(service.save(newBook),true); //A created book typically has not yet been loaned out


        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseWrapper<>(responseBody,"v2") );
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<BookResponseDTOv2>>> getAll() {

        List<BookResponseDTOv2> books =
                service.findAll().stream()
                        .map(book -> mapper.bookToDTOV2(
                                book,
                                loanService.bookIsAvailable(book.getId())
                        ))
                        .toList();

        return ResponseEntity.ok(
                new ApiResponseWrapper<>(books, "v2")
        );
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
    public ResponseEntity<ApiResponseWrapper<BookResponseDTOv2>> getBookByID(@PathVariable Long id){
        BookResponseDTOv2 data = mapper.bookToDTOV2(
                        service.getBookByID(id),
                        loanService.bookIsAvailable(id));

        return ResponseEntity.ok(new ApiResponseWrapper<BookResponseDTOv2>(
                data,"v2"
        ));
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
    public ResponseEntity<ApiResponseWrapper<BookResponseDTOv2>> update(
            @PathVariable Long id,
            @RequestBody UpdateBookRequestDTO dto
    ) {
        Book updated = service.update(id, dto);
        Boolean available = loanService.bookIsAvailable(id);
        return ResponseEntity.ok(new ApiResponseWrapper<>(mapper.bookToDTOV2(updated,available),"v2"));


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