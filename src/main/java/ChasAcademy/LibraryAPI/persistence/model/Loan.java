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
    Long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    Book book;

    @Column
    @PastOrPresent
    LocalDate loanDate = LocalDate.now();

    @Column
    LocalDate returnDate = null;

    public Loan(Book book){
        this.book = book;
    }

}
