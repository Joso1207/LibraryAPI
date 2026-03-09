package ChasAcademy.LibraryAPI.persistence.repository;

import ChasAcademy.LibraryAPI.persistence.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}
