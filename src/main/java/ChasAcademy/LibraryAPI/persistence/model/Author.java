package ChasAcademy.LibraryAPI.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
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
    private Long id;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "Name of Author cannot be Blank")
    private String name;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Book> writtenWorks = new ArrayList<>();

    public Author(String name){
        this.name = name;
        this.writtenWorks = new ArrayList<>();
    }

}
