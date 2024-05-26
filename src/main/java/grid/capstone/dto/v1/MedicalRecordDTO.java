package grid.capstone.dto.v1;

import grid.capstone.model.Prescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {

    @NotNull(message = "Doctor ID is required")
    @Positive(message = "Doctor ID must be a positive value")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    @Positive(message = "Patient ID must be a positive value")
    private Long patientId;

    @NotNull(message = "Appointment ID is required")
    @Positive(message = "Appointment ID must be a positive value")
    private Long appointmentId;

    @NotNull(message = "Check in date is required")
    @PastOrPresent(message = "Check in date cannot be in the future")
    private LocalDate checkInDate;

    private String notes;

    private String disease;

    private String status;

    @NotBlank(message = "Room number is required")
    private String roomNo;

    private List<Prescription> prescriptions;

}
