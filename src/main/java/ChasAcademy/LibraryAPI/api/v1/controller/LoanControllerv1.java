package ChasAcademy.LibraryAPI.api.v1.controller;

import ChasAcademy.LibraryAPI.api.core.dto.LoanDTO;
import ChasAcademy.LibraryAPI.api.v1.mapper.LoanMapperv1;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/loans")
public class LoanControllerv1 {

    private final LoanService service;
    private final LoanMapperv1 loanMapperv1;

    public LoanControllerv1(LoanService service,LoanMapperv1 mapperv1){
        this.service = service;
        this.loanMapperv1 = mapperv1;
    }

    @PostMapping
    public ResponseEntity<LoanDTO> newLoan(Long bookID){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        loanMapperv1.loanToDTO(service.addLoan(bookID))
                );
    }

    @GetMapping
    public List<LoanDTO> allLoans(){
        return service.activeLoans().stream().map(loanMapperv1::loanToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> findLoanByID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(loanMapperv1.loanToDTO(service.getLoanByID(id)));
    }

    @GetMapping("/history")
    public List<LoanDTO> historicalLoans(){
        return service.getAllLoans().stream().map(loanMapperv1::loanToDTO).toList();
    }




}
