package ChasAcademy.LibraryAPI.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.PROTECTED)
    Long id;

    @Column(nullable = false,unique = true)
    @NotNull(message = "Name cannot be null")
    String name;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    List<Book> writtenWorks = new ArrayList<>();

    public Author(String name){
        this.name = name;
        this.writtenWorks = new ArrayList<>();
    }

}
