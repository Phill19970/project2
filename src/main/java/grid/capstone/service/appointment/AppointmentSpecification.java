package grid.capstone.service.appointment;

import grid.capstone.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

public class AppointmentSpecification {

    /**
     * Method create a criteria which checks for the specific
     * date the client is looking for.
     *
     * @param dateFilter date by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasDate(LocalDate dateFilter) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("appointmentDate"),
                        dateFilter
                );
    }

    /**
     * Method create a criteria which checks for the specific
     * doctor the client is looking for.
     *
     * @param doctorId id by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasDoctor(Long doctorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("doctor").get("id"),
                        doctorId
                );
    }

    /**
     * Method create a criteria which checks for the specific
     * patient the client is looking for.
     *
     * @param patientId id by which the wants to see their appointments
     * @return
     */
    public static Specification<Appointment> hasPatient(Long patientId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("patient").get("id"),
                        patientId
                );
    }

}
