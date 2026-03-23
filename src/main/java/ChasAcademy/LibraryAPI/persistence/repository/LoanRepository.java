package ChasAcademy.LibraryAPI.persistence.repository;

import ChasAcademy.LibraryAPI.persistence.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    Optional<Loan> findByBookId(Long id);
    List<Loan> findByReturnDateIsNull();
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long id);

}
