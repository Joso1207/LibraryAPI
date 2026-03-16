package ChasAcademy.LibraryAPI.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;

@Entity
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Title cannot be null")
    private String title;

    @Column(nullable = false)
    @ManyToOne
    @NotNull(message = "Author cannot be null")
    private Author author;

    @Column
    @ISBN
    private String isbn;

    @Column
    private Integer publishedYear;

    public Book(String title,Author author){
        this.title = title;
        this.author = author;
    }

    public Book(String title,Author author,String isbn,Integer publishedYear){
        this(title,author);
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }


}
