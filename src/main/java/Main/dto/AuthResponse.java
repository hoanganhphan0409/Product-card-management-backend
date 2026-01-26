package Main.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private String message;
}