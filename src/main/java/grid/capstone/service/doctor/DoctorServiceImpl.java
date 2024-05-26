package grid.capstone.service.doctor;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.DoctorMapper;
import grid.capstone.model.Doctor;
import grid.capstone.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    private final PasswordEncoder passwordEncoder;



    @Override
    public Doctor getDoctor(Long doctorId) {

        //TODO: Add exception handling when id is not found
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + doctorId + " does not exist"));
    }


    @Override
    public Page<DoctorDTO> getAllDoctors(Optional<String> specialization, Optional<String> department, Optional<String> name, Integer size, Integer page) {

        Specification<Doctor> doctorSpecification = Specification
                .where(specialization
                        .map(DoctorSpecification::hasSpecialization)
                        .orElse(null)
                )
                .and(department
                        .map(DoctorSpecification::inDepartment)
                        .orElse(null)
                )
                .and(name
                        .map(DoctorSpecification::hasName)
                        .orElse(null)
                );


        Pageable pageable = PageRequest.of(page, size);

        Page<Doctor> doctorPage = doctorRepository.findAll(doctorSpecification, pageable);
        return doctorPage.map(doctorMapper::toDTO);
    }

    @Override
    public HttpStatus saveDoctor(DoctorSignUp doctorSignUp) {
        if (doctorRepository.existsByEmail(doctorSignUp.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        }


        Doctor doctor = doctorMapper.toEntity(doctorSignUp);
        doctor.setPassword(passwordEncoder.encode(doctorSignUp.getPassword()));

        doctorRepository.save(doctor);


        return HttpStatus.CREATED;
    }
}
