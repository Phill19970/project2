package grid.capstone.service.doctor;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Optional;


/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

public interface DoctorService {


    /**
     * This method searches for a specific
     * doctor based on the id passed
     *
     * @param doctorId Id of the doctor
     * @return A doctor object
     * @throws ResourceNotFoundException when the id is not found
     */
    Doctor getDoctor(Long doctorId);

    /**
     *
     * Method retrieves a list of doctor objects based on the
     * query parameters passed from the client
     *
     * @param specialization Doctor's Specialization
     * @param department Doctor's Department
     * @param name Doctor's name
     * @param size size of the page
     * @param page page to be returned
     */
    Page<DoctorDTO> getAllDoctors(Optional<String> specialization, Optional<String> department, Optional<String> name, Integer size, Integer page);

    /**
     *
     * Sign's up a doctor in the system
     *
     * @param doctorSignUp sign up POJO
     * @return HttpStatus
     */
    HttpStatus saveDoctor(DoctorSignUp doctorSignUp);
}
