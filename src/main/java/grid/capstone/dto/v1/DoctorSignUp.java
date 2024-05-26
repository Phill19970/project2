package grid.capstone.dto.v1;


import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;


import java.util.List;


/**
 * @author Javaughn Stephenson
 * @since 17/07/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSignUp {

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be greater than 8 characters")
    private String password;

    @NotBlank(message = "Username is required")
    private String name;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String biography;

    private List<String> skills;

    private String specialization;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Character gender;


    private String address;


}
