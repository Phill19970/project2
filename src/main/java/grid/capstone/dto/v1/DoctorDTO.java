package grid.capstone.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Javaughn Stephenson
 * @since 19/06/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Username is required")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;

    @Size(max = 500, message = "Biography should be at most 500 characters")
    private String biography;
}
