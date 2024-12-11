package org.kvn.DigitalLibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCreationRequest {

    private String userName;

    @NotBlank(message = "user email must not be blank")
    private String userEmail;

    private String password;

    private String userAddress;

    private String userPhone;

    public User toUser() {
        return User.builder()
                .name(this.userName)
                .phoneNo(this.userPhone)
                .email(this.userEmail)
                .address(this.userAddress)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
