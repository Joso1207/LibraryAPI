package ChasAcademy.LibraryAPI.persistence.repository;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Long, Author> {

}
