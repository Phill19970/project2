package grid.capstone.repository;

import grid.capstone.model.Appointment;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);

}
