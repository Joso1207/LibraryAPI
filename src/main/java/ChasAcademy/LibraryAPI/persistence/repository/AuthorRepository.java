package ChasAcademy.LibraryAPI.persistence.repository;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {

    public Optional<Author> findByName(String name);

}
