package org.kvn.DigitalLibrary.dto.response;

import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class BookCreationResponse {

    private String bookName;
    private String bookNo;
    private double securityAmount;
}
