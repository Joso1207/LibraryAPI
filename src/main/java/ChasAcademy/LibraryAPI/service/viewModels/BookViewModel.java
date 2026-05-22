package ChasAcademy.LibraryAPI.service.viewModels;

import ChasAcademy.LibraryAPI.persistence.model.Book;
import lombok.Builder;

@Builder
public record BookViewModel(Long id, String title, String author, Long authorID, int authorBookAmount, String isbn, Integer yearPublished){

    public BookViewModel(Book book){
        this(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getAuthor().getId(),
                book.getAuthor().getWrittenWorks().size(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }

    public BookViewModel(Long id, String title, String author, Long authorID,int authorBookAmount , String isbn, Integer yearPublished) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorID = authorID;
        this.authorBookAmount = authorBookAmount;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
    }
};
