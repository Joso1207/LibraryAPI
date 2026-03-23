package ChasAcademy.LibraryAPI.api.v1.mapper;

import ChasAcademy.LibraryAPI.api.core.dto.LoanDTO;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapperv1 {

    public LoanDTO loanToDTO(Loan loan){
        return LoanDTO.builder()
                .id(loan.getId())
                .bookID(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .build();
    }

}
