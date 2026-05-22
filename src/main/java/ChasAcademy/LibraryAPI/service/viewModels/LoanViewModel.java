package ChasAcademy.LibraryAPI.service.viewModels;

import ChasAcademy.LibraryAPI.persistence.model.Loan;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LoanViewModel (Long id, BookViewModel book, LocalDate checkoutDate,LocalDate returnDate ){

    public LoanViewModel(Loan loan){
        this(
                loan.getId(),
                new BookViewModel(loan.getBook()),
                loan.getLoanDate(),
                loan.getReturnDate()
        );
    }


    public LoanViewModel(Long id, BookViewModel book, LocalDate checkoutDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
    }
}
