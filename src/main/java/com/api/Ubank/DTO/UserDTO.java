package com.api.Ubank.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private Long dni;
    private String email;
    private String password;
}
