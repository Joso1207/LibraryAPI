package ChasAcademy.LibraryAPI.api.core.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LoanDTO(Long id, Long bookID, String bookTitle, LocalDate loanDate,LocalDate returnDate) {
}
