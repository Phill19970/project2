package grid.capstone.repository;

import grid.capstone.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByDoctorId(Long doctorId);
    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);
}
