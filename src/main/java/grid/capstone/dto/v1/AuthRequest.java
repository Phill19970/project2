package grid.capstone.dto.v1;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Javaughn Stephenson
 * @since 18/07/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @Email(message = "Email is required")
    private String email;
    @NotBlank
    private String password;
}
