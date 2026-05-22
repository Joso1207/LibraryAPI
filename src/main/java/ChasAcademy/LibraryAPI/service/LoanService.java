package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotAvailableException;
import ChasAcademy.LibraryAPI.api.core.exceptions.BookNotFoundException;
import ChasAcademy.LibraryAPI.api.core.exceptions.LoanNotFoundException;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository repo;
    private final BookRepository bookRepo;

    public LoanService(LoanRepository repo,BookRepository bookRepo){
        this.repo = repo;
        this.bookRepo = bookRepo;
    }

    @Cacheable("loans")
    public Page<Loan> getLoans(Pageable pageable){
        return repo.findAll(pageable);
    }

    @Cacheable(value = "loan", key="#id")
    public Loan getLoanByID(Long id){


        return repo.findById(id).orElseThrow(
                ()->new LoanNotFoundException(id)
        );
    }

    public Optional<Loan> findActiveLoan(Long bookID){
        return repo.findByBookIdAndReturnDateIsNull(bookID);
    }

    public Boolean bookIsAvailable(Long bookID){
        return !repo.existsByBookIdAndReturnDateIsNull(bookID);
    }

    @Cacheable("activeLoans")
    public List<Loan> activeLoans(){
        return repo.findByReturnDateIsNull();
    }


    public Page<Loan> activeLoansPage(Pageable pageable){
        return repo.findByReturnDateIsNull(pageable);
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

        Book book = bookRepo.findById(bookID).orElseThrow(() -> new BookNotFoundException(bookID));

        if(findActiveLoan(bookID).isPresent()){
            throw new BookNotAvailableException(bookID);
        }

        return repo.save(new Loan(book));
    }



}
