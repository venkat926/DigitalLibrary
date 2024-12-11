package org.kvn.DigitalLibrary.repository;

import jakarta.validation.constraints.NotBlank;
import org.kvn.DigitalLibrary.model.Txn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepository extends JpaRepository<Txn, Integer> {
    Txn findByTxnId(@NotBlank String txnId);
}

