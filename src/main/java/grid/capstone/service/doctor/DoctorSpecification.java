package grid.capstone.service.doctor;

import grid.capstone.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Class is for the doctor's specifications
 * which are used to get the doctor object which corresponds
 * with the query params passed
 *
 * @author Javaughn Stephenson
 * @since 19/06/2023
 */

public class DoctorSpecification {

    /**
     * Method create a criteria which checks for the specific
     * specialization the client is looking for.
     *
     * @param specialization
     * @return
     */
    public static Specification<Doctor> hasSpecialization(String specialization) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("specialization")),
                        specialization.toLowerCase()
                );
    }


    /**
     * Method create a criteria which checks for the specific
     * department the client is looking for.
     *
     * @param department
     * @return
     */
    public static Specification<Doctor> inDepartment(String department) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("department")),
                        department.toLowerCase()
                );
    }


    /**
     * Method create a criteria which checks for the specific
     * username the client is looking for.
     *
     * @param username
     * @return
     */
    public static Specification<Doctor> hasName(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("username")),
                        username.toLowerCase()
                );
    }
}
