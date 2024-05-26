package grid.capstone.service.appointment;

import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.model.Appointment;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

public interface AppointmentService {

    /**
     * Creates a new appointment between the doctor and patient
     *
     * @param appointmentDTO DTO for the appointment object
     * @return HttpStatus code
     */
    HttpStatus createAppointment(AppointmentDTO appointmentDTO);

    /**
     *
     * Returns the upcoming appointments of either the patient
     * or the doctor
     *
     * @param dateFilter date which to filter the results by
     * @param patientId id of the patient with appointments
     * @param doctorId id of the doctor with appointments
     * @return List of appointments
     */
    List<Appointment> getFilteredAppointments(Optional<LocalDate> dateFilter, Optional<Long> patientId, Optional<Long> doctorId);


    /**
     * Updates an existing appointment
     *
     * @param appointmentId appointment id
     * @param appointmentDTO appointment object with updated values
     * @return HttpStatus code
     */
    HttpStatus updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO);
}

