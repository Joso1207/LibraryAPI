package ChasAcademy.LibraryAPI.api.dto;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NewBookRequestDTO {
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublished;

}
