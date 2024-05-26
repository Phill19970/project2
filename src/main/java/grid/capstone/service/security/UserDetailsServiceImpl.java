package grid.capstone.service.security;

import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import grid.capstone.repository.DoctorRepository;
import grid.capstone.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 18/07/2023
 */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Doctor> doctor = doctorRepository.findByEmail(email);

        if (doctor.isPresent()) {
            return doctor.get();
        }

        Optional<Patient> patient = patientRepository.findByEmail(email);

        if (patient.isPresent()) {
            return patient.get();
        }


        throw new ResourceNotFoundException("User not found");
    }
}
