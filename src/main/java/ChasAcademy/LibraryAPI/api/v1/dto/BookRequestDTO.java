package ChasAcademy.LibraryAPI.api.v1.dto;

import lombok.*;


@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BookRequestDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublished;




}
