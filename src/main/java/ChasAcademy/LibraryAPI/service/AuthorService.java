package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Cacheable("authors")
    public Page<Author> findAll(Pageable pageable){
        return authorRepository.findAll(pageable);
    }

    @Cacheable(value = "authors", key = "#id")
    public Author findAuthorByID(Long id){
        return authorRepository.findById(id).orElseThrow(
                () -> new AuthorNotFoundException(id)
        );
    }

    @CachePut(value = "authors", key="#result.id")
    public Author addAuthor(NewAuthorDTO dto){
        return authorRepository.save(
                Author.builder()
                        .name(dto.name())
                        .build()
        );
    }

}
