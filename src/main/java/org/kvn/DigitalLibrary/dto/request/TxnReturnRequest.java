package org.kvn.DigitalLibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TxnReturnRequest {
    @NotBlank
    private String bookNo;
    @NotBlank
    private String txnId;
    @NotBlank
    private String userEmail;
}
