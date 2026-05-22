package ChasAcademy.LibraryAPI.service.viewModels;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record AuthorViewModel (Long id, String name, Map<String,BookViewModel> writtenWorks){

    public AuthorViewModel(Author author) {
        this(author.getId(),
                author.getName(),
                author.getWrittenWorks().stream()
                        .map(BookViewModel::new)
                        .collect(Collectors.toMap(BookViewModel::isbn,viewModel->viewModel)));
    }

    public AuthorViewModel(Long id, String name, Map<String, BookViewModel> writtenWorks) {
        this.id = id;
        this.name = name;
        this.writtenWorks = writtenWorks;
    }
}
