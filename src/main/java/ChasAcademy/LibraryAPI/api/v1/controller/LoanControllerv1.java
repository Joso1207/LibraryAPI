package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.LoanDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.ApiError;
import ChasAcademy.LibraryAPI.api.v1.mapper.LoanMapperv1;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/loans")
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
public class LoanControllerv1 {

    private final LoanService service;
    private final LoanMapperv1 loanMapperv1;

    public LoanControllerv1(LoanService service,LoanMapperv1 mapperv1){
        this.service = service;
        this.loanMapperv1 = mapperv1;
    }

    @Operation(summary = "Create new Loan")
    @ApiResponse(responseCode = "201", description = "Set a book as loaned out")
    @PostMapping
    public ResponseEntity<LoanDTO> newLoan(@RequestBody Long bookID){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        loanMapperv1.loanToDTO(service.addLoan(bookID))
                );
    }

    @Operation(summary = "Get all loans currently listed as active")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<LoanDTO> allLoans(){
        return service.activeLoans().stream().map(loanMapperv1::loanToDTO).toList();
    }

    @Operation(summary = "Get all specific loan")
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
    public ResponseEntity<LoanDTO> findLoanByID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(loanMapperv1.loanToDTO(service.getLoanByID(id)));
    }

    @Operation(summary = "Get all loans active or inactive")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/history")
    public List<LoanDTO> historicalLoans(){
        return service.getAllLoans().stream().map(loanMapperv1::loanToDTO).toList();
    }




}
