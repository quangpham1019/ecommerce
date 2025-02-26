package org.ecommerce.user.application.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfileDTO {

    private Long id;
    private String username;
    private String email;
    private List<String> roleNames;
}