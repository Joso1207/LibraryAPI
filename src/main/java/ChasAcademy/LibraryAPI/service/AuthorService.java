package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.api.core.mapper.JsonPageImpl;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.service.viewModels.AuthorViewModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Cacheable("authors")
    public Page<AuthorViewModel> findAll(Pageable pageable){
        return authorRepository.findAll(pageable).map(AuthorViewModel::new);
    }


    @Cacheable(value = "author", key = "#id")
    public AuthorViewModel findAuthorByID(Long id){
        return new AuthorViewModel(authorRepository.findById(id).orElseThrow(
                () -> new AuthorNotFoundException(id)
        ));
    }

    @CacheEvict(value = "authors", allEntries = true)
    public Author addAuthor(NewAuthorDTO dto){
        return authorRepository.save(
                Author.builder()
                        .name(dto.name())
                        .build()
        );
    }

}
