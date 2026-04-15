package ChasAcademy.LibraryAPI.api.v2.controller;

import ChasAcademy.LibraryAPI.api.core.ApiResponseWrapper;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.mapper.BookMapperV1;
import ChasAcademy.LibraryAPI.api.v2.dto.BookRequestDTOv2;
import ChasAcademy.LibraryAPI.api.v2.mapper.BookMapperV2;
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
    public ResponseEntity<ApiResponseWrapper<BookRequestDTOv2>> addBook(@Valid @RequestBody NewBookRequestDTO postRequest){
        NewBookRequestDTO newBook = mapper.v2dtoToBookRequest(postRequest);
        BookRequestDTOv2 responseBody = mapper.bookToDTOV2(service.save(newBook),true); //A created book typically has not yet been loaned out


        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseWrapper<>(responseBody,"v2") );
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<BookRequestDTOv2>>> getAll() {

        List<BookRequestDTOv2> books =
                service.findAll().stream()
                        .map(book -> mapper.bookToDTOV2(
                                book,
                                loanService.findActiveLoan(book.getId()).isPresent()
                        ))
                        .toList();

        return ResponseEntity.ok(
                new ApiResponseWrapper<>(books, "v2")
        );
    }

    @Operation(summary = "Get specific book")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<BookRequestDTOv2>> getBookByID(@PathVariable Long id){
        BookRequestDTOv2 data = mapper.bookToDTOV2(
                        service.getBookByID(id),
                        loanService.findActiveLoan(id).isPresent());

        return ResponseEntity.ok(new ApiResponseWrapper<BookRequestDTOv2>(
                data,"v2"
        ));
    }



}