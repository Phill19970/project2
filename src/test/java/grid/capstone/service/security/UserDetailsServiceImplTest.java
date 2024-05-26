package grid.capstone.service.security;

import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import grid.capstone.repository.DoctorRepository;
import grid.capstone.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;


    @BeforeEach
    void setUp() {
    }

    @Test
    public void testLoadUserByUsername_WhenDoctorExists_ShouldReturnDoctor() {
        // Given
        String email = "doctor@example.com";
        Doctor doctor = new Doctor();
        doctor.setEmail(email);

        given(doctorRepository.findByEmail(email)).willReturn(Optional.of(doctor));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(Doctor.class);
        assertThat(email).isEqualTo(userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_WhenPatientExists_ShouldReturnPatient() {
        // Given
        String email = "patient@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);

        given(doctorRepository.findByEmail(email)).willReturn(Optional.empty());
        given(patientRepository.findByEmail(email)).willReturn(Optional.of(patient));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(Patient.class);
        assertThat(email).isEqualTo(userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_WhenUserNotExists_ShouldThrowException() {
        // Given
        String email = "nonexistent@example.com";

        given(doctorRepository.findByEmail(email)).willReturn(Optional.empty());
        given(patientRepository.findByEmail(email)).willReturn(Optional.empty());

        // When and Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }


}