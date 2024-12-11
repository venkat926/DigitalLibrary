package org.kvn.DigitalLibrary.service.impl;

import org.kvn.DigitalLibrary.dto.request.TxnRequest;
import org.kvn.DigitalLibrary.dto.request.TxnReturnRequest;
import org.kvn.DigitalLibrary.enums.TxnStatus;
import org.kvn.DigitalLibrary.exception.BookException;
import org.kvn.DigitalLibrary.exception.TxnException;
import org.kvn.DigitalLibrary.exception.UserException;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.model.Txn;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.TxnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {

    @Autowired
    private TxnRepository txnRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Value("${user.valid.days}")
    private int validUpto;

    @Value("${user.valid.finePerDay}")
    private int finePerDay;

    @Transactional(rollbackFor = {TxnException.class, BookException.class})
    public String create(TxnRequest txnRequest) throws BookException, UserException {
        // check if the user is valid or not
        User userFromDb = userService.checkIfUserIsValid(txnRequest.getUserEmail());
        if (userFromDb == null) {
            throw new UserException("User is not valid");
        }

        // check if book is valid or not
        Book bookFromDb = bookService.checkIfBookIsValid(txnRequest.getBookNo());
        if (bookFromDb == null) {
            throw new BookException("Book id not valid");
        }

        // check if the book is available or assigned to someone
        if (bookFromDb.getUser() != null) {
            throw new BookException("Book is not available to issue");
        }

        // issue book
        return createTxn(userFromDb, bookFromDb);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private String createTxn(User userFromDb, Book bookFromDb) {
        String txnId = UUID.randomUUID().toString();
        Txn txn = Txn.builder()
                .txnId(txnId)
                .user(userFromDb)
                .book(bookFromDb)
                .txnStatus(TxnStatus.ISSUED)
                .issuedDate(new Date())
                .build();
        txnRepository.save(txn);
        bookService.markBookUnavailable(bookFromDb, userFromDb);
        return txnId;
    }

    @Transactional(rollbackFor = {BookException.class, UserException.class, TxnException.class})
    public double returnBookTxn(TxnReturnRequest txnReturnRequest) throws BookException, UserException {
        // check if the user is valid or not
        User userFromDb = userService.checkIfUserIsValid(txnReturnRequest.getUserEmail());
        if (userFromDb == null) {
            throw new UserException("User is not valid");
        }

        // check if book is valid or not
        Book bookFromDb = bookService.checkIfBookIsValid(txnReturnRequest.getBookNo());
        if (bookFromDb == null) {
            throw new BookException("Book id not valid");
        }

        if (bookFromDb.getUser() != null && bookFromDb.getUser().equals(userFromDb)) {
            Txn txnFromDb = txnRepository.findByTxnId(txnReturnRequest.getTxnId());
            if (txnFromDb == null) {
                throw new TxnException("No txn has been found in Db with this txnID");
            }

            Double amount = calculateSettlementAmount(txnFromDb, bookFromDb);
            if (amount == bookFromDb.getSecurityAmount()) {
                txnFromDb.setTxnStatus(TxnStatus.RETURNED);
            } else {
                txnFromDb.setTxnStatus(TxnStatus.FINED);
            }
            txnFromDb.setSettlementAmount(amount);
            // mark the book available
            bookFromDb.setUser(null);
            txnRepository.save(txnFromDb);
            return amount;
        } else {
            throw new TxnException("Book is assigned to someone else, or not at all assigned");
        }

    }

    private Double calculateSettlementAmount(Txn txnFromDb, Book bookFromDb) {
        long issueTime = txnFromDb.getIssuedDate().getTime();
        long returnTime = System.currentTimeMillis();
        long diff = returnTime - issueTime;
        int daysPassed = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (daysPassed > validUpto) {
            int fineAmount = (daysPassed - validUpto) * finePerDay;
            return bookFromDb.getSecurityAmount()-fineAmount;
        } else {
            return bookFromDb.getSecurityAmount();
        }
    }
}
