package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.exceptions.AuthorNotFoundException;
import ChasAcademy.LibraryAPI.api.core.mapper.AuthorMapper;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private AuthorMapper mapper;
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> findAll(){
        return authorRepository.findAll().stream().map(mapper::authorToDTO).toList();
    }

    public AuthorDTO findAuthorByID(Long id){
        Author entity = authorRepository.findById(id).orElseThrow(
                () -> new AuthorNotFoundException(id)
        );
        return mapper.authorToDTO(entity);
    }



}
