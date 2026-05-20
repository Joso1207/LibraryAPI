package ChasAcademy.LibraryAPI.persistence.repository;

import ChasAcademy.LibraryAPI.persistence.model.Loan;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    Optional<Loan> findByBookId(Long id);
    List<Loan> findByReturnDateIsNull();
    Page<Loan> findByReturnDateIsNull(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long id);

    Boolean existsByBookIdAndReturnDateIsNull(Long ID);

}
