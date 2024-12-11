package org.kvn.DigitalLibrary.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TxnRequest {

    @NotBlank(message = "user email must not be blank")
    private String userEmail;

    @NotBlank(message = "bookNo must not be blank")
    private String bookNo;
}
