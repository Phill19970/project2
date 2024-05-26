package grid.capstone.service.patient;

import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.model.Patient;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

public interface PatientService {

    /**
     * Get a specific patient based on the
     * patientId passed
     *
     * @param patientId
     * @return A patient object
     * @throws ResourceNotFoundException when the id cannot be found
     */
    Patient getPatient(Long patientId);

    /**
     * Save a patient to the database
     *
     * @param patientSignUp patient DTO
     * @param doctorId id of the doctor
     * @return HttpStatus code
     */
    HttpStatus savePatient(PatientSignUp patientSignUp, Optional<Long> doctorId);

    /**
     * Get all patients associate with a doctor
     * @param doctorId id of the doctor
     * @return list of patients
     */
    List<Patient> getAllPatients(Long doctorId);
}
