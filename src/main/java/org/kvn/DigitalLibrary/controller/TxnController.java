package org.kvn.DigitalLibrary.controller;

import org.kvn.DigitalLibrary.dto.request.TxnRequest;
import org.kvn.DigitalLibrary.dto.request.TxnReturnRequest;
import org.kvn.DigitalLibrary.exception.BookException;
import org.kvn.DigitalLibrary.exception.UserException;
import org.kvn.DigitalLibrary.service.impl.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/txn")
public class TxnController {

    @Autowired
    private TxnService txnService;

    @PostMapping("/issue")
    public ResponseEntity<String> createTxn(@RequestBody @Validated TxnRequest txnRequest) throws BookException, UserException {
        String txnId = txnService.create(txnRequest);
        if (txnId!=null && !txnId.isEmpty()) {
            return new ResponseEntity<>(txnId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return")
    public Double returnTxn(@RequestBody TxnReturnRequest txnReturnRequest) throws BookException, UserException {
        return txnService.returnBookTxn(txnReturnRequest);
    }
}
