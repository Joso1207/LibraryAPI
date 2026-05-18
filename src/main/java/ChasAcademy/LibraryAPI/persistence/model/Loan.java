package ChasAcademy.LibraryAPI.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column
    @PastOrPresent
    @Builder.Default
    private LocalDate loanDate = LocalDate.now();

    @Column
    @Builder.Default
    private LocalDate returnDate = null;

    public Loan(Book book){
        this.book = book;
        this.loanDate = LocalDate.now();
    }

}
