package grid.capstone.service.patient;

import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.PatientMapper;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import grid.capstone.repository.DoctorRepository;
import grid.capstone.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;



    @Override
    public Patient getPatient(Long patientId) {


        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + patientId + " does not exist"));
    }

    @Override
    public HttpStatus savePatient(PatientSignUp patientSignUp, Optional<Long> doctorId) {
        if (patientRepository.existsByEmail(patientSignUp.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        //Mapping the DTO to the entity and setting the doctor
        //to the entity
        Patient patient = patientMapper.toEntity(patientSignUp);
        patient.setPassword(passwordEncoder.encode(patientSignUp.getPassword()));


        //Check if doctor exists
        if (doctorId.isPresent()) {

            if (!doctorRepository.existsById(doctorId.get())) {
                throw new ResourceNotFoundException("Doctor with id " + doctorId.get() + " does not exists");
            }
            else {
                patient.setDoctor(
                        Doctor.builder()
                                .id(doctorId.get())
                                .build()
                );
            }

        }

        patientRepository.save(patient);

        return HttpStatus.CREATED;
    }

    @Override
    public List<Patient> getAllPatients(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor with id " + doctorId + " does not exist");
        }


        return patientRepository
                .findAllByDoctorId(doctorId);
    }
}
