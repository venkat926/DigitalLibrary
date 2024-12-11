package org.kvn.DigitalLibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.model.Book;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BookCreationRequest {

    @NotBlank(message = "book title must not be blank")
    private String bookTitle;

    @NotBlank(message = "book No. must not be blank")
    private String bookNo;

    @NotNull(message = "book type must not be blank")
    private BookType bookType;

    @Positive(message = "Positive values are expected")
    private  double securityAmount;

    @NotBlank(message = "author Name must not be blank")
    private String authorName;

    @NotBlank(message = "author Email must not be blank")
    private String authorEmail;

    public Book toBook() {
        return Book.builder()
                .title(this.bookTitle)
                .bookNo(this.bookNo)
                .bookType(this.bookType)
                .securityAmount(this.securityAmount)
                .build();
    }
}
