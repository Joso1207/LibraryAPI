package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.exceptions.LoanNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository repo;
    private final BookService bookService;

    public LoanService(LoanRepository repo,BookService bookService){
        this.repo = repo;
        this.bookService = bookService;
    }

    public List<Loan> getAllLoans(){
        return repo.findAll();
    }

    public List<Loan> getActiveLoans(){
        return repo.findByReturnDateIsNull();
    }

    public Loan getLoanByID(Long id){
        return repo.findByBookId(id).orElseThrow(
                ()->new LoanNotFoundException(id)
        );
    }

    public Optional<Loan> findActiveLoan(Long bookID){
        return repo.findByBookIdAndReturnDateIsNull(bookID);
    }

    public List<Loan> activeLoans(){
        return repo.findByReturnDateIsNull();
    }

    @Transactional
    public Loan addLoan(Long bookID){
        if(findActiveLoan(bookID).isPresent()){
            //Book not available Exception
        }
        Book book = bookService.getBookByID(bookID); //Throws book unavailable if book does not exist
        return repo.save(new Loan(book));
    }



}
