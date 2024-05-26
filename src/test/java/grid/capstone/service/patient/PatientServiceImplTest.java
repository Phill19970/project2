package grid.capstone.service.patient;

import grid.capstone.dto.v1.PatientSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.PatientMapper;
import grid.capstone.mapper.PatientMapperImpl;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import grid.capstone.repository.DoctorRepository;
import grid.capstone.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    PatientService patientService;

    @Mock
    PatientRepository patientRepository;
    @Mock
    DoctorRepository doctorRepository;

    PatientMapper patientMapper = new PatientMapperImpl();

    Patient testPatient;
    PatientSignUp testPatientDTO;
    Doctor testDoctor;

    @BeforeEach
    void setUp() {
        patientService = new PatientServiceImpl(patientRepository, doctorRepository, patientMapper, passwordEncoder);

        testPatient = Patient.builder()
                .name("name")
                .age(1)
                .address("address")
                .phoneNumber("1234567890")
                .bloodGroup("A")
                .religion("Religion")
                .occupation("Occupation")
                .gender('M')
                .maritalStatus("Single")
                .description("Description")
                .password("password")
                .doctor(Doctor.builder().build())
                .appointments(List.of())
                .medicalRecords(List.of())
                .email("test@gmail.com")
                .build();

        testPatientDTO = PatientSignUp.builder()
                .email("test@gmail.com")
                .password("test")
                .name("name")
                .address("address")
                .phoneNumber("1234567890")
                .age(1)
                .bloodGroup("A")
                .religion("religion")
                .occupation("Work")
                .gender('M')
                .maritalStatus("Single")
                .description("description")
                .build();

        testDoctor = Doctor.builder().build();
    }

    @Test
    void getPatient() {
        given(patientRepository.findById(anyLong()))
                .willReturn(Optional.of(testPatient));

        Patient patient = patientService.getPatient(1L);

        assertThat(patient).isEqualTo(testPatient);
    }

    @Test
    public void testGetPatientWhenPatientDoesNotExist() {
        Long patientId = 2L;

        // Mock the patientRepository behavior to return an empty Optional (patient not found)
        given(patientRepository.findById(patientId)).willReturn(Optional.empty());

        // Call the method you want to test and expect an exception
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatient(patientId));
        // Add more assertions or verifications as per your test case requirements
    }

    @Test
    void savePatient() {
        given(patientRepository.existsByEmail(anyString()))
                .willReturn(false);

        given(doctorRepository.existsById(anyLong()))
                .willReturn(true);

        HttpStatus httpStatus = patientService.savePatient(testPatientDTO, Optional.of(1L));

        then(patientRepository).should(times(1)).save(any(Patient.class));
        assertThat(httpStatus).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testSavePatient_EmailAlreadyExists() {
        // Arrange
        given(patientRepository.existsByEmail(anyString()))
                .willReturn(true);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> patientService.savePatient(testPatientDTO, Optional.empty()));
    }

    @Test
    public void testSavePatient_DoctorIdDoesNotExist() {
        // Arrange
        given(patientRepository.existsByEmail(anyString())).willReturn(false);
        given(doctorRepository.existsById(anyLong())).willReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> patientService.savePatient(testPatientDTO, Optional.of(1L)));
    }

    @Test
    void getAllPatients() {
        given(doctorRepository.existsById(anyLong()))
                .willReturn(true);

        given(patientRepository.findAllByDoctorId(anyLong()))
                .willReturn(List.of(testPatient));


        List<Patient> allPatients = patientService.getAllPatients(1L);


        assertThat(allPatients).isEqualTo(List.of(testPatient));
    }

    @Test
    public void testGetAllPatients_DoctorIdDoesNotExist() {
        Long doctorId = 1L;
        given(doctorRepository.existsById(doctorId)).willReturn(false);


        assertThrows(ResourceNotFoundException.class, () -> patientService.getAllPatients(doctorId));
    }
}