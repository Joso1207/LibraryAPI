package ChasAcademy.LibraryAPI.api.dto;

import lombok.Builder;


@Builder
public class BookRequestDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublished;


}
