package grid.capstone.dto.v1;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be in the future or present")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Reason is required")
    private String reason;

}
