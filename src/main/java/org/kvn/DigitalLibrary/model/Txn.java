package org.kvn.DigitalLibrary.model;

import jakarta.persistence.*;
import lombok.*;
import org.kvn.DigitalLibrary.enums.TxnStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Txn extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String txnId;

    private TxnStatus txnStatus;

    private Double settlementAmount;

    private Date issuedDate;

    private Date submittedDate;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Book book;
}
