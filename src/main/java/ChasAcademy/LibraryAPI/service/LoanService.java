package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotAvailableException;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.api.core.exceptions.LoanNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable("loans")
    public List<Loan> getAllLoans(){
        return repo.findAll();
    }

    @Cacheable(value = "loan", key="#id")
    public Loan getLoanByID(Long id){


        return repo.findById(id).orElseThrow(
                ()->new LoanNotFoundException(id)
        );
    }

    @Cacheable(value = "activeLoan", key="#bookID")
    public Optional<Loan> findActiveLoan(Long bookID){
        return repo.findByBookIdAndReturnDateIsNull(bookID);
    }

    @Cacheable("activeLoans")
    public List<Loan> activeLoans(){
        return repo.findByReturnDateIsNull();
    }


    @Caching(
            put = {
                    @CachePut(value = "loan", key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = "activeLoans", allEntries = true),
                    @CacheEvict(value = "activeLoan", allEntries = true)
            }
    )
    @Transactional
    public synchronized Loan addLoan(Long bookID){

        Book book = bookService.getBookByID(bookID); //Throws bookNotFound if book does not exist

        if(findActiveLoan(bookID).isPresent()){
            throw new BookNotAvailableException(bookID);
        }

        return repo.save(new Loan(book));
    }



}
