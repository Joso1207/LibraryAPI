package ChasAcademy.LibraryAPI.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.PROTECTED)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String username;
    @Column
    @Size(min = 8)
    private String password;
    @Column
    private String role;

}
