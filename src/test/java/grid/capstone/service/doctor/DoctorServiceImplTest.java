package grid.capstone.service.doctor;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.DoctorMapper;
import grid.capstone.model.Doctor;
import grid.capstone.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    DoctorRepository doctorRepository;

    DoctorService doctorService;

    @Mock
    DoctorMapper doctorMapper;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    Doctor doctor;

    DoctorSignUp doctorSignUp;

    @BeforeEach
    void setUp() {

        doctorService = new DoctorServiceImpl(doctorRepository, doctorMapper, passwordEncoder);

        doctor = Doctor.builder()
                .id(1L)
                .password("")
                .email("test")
                .build();

        doctorSignUp = DoctorSignUp.builder()
                .email("test@mail.com")
                .password("test")
                .build();

    }

    @Test
    void getDoctorWithValidId_ShouldReturnDoctor() {

        // Given
        Long validDoctorId = 1L;

        // Mocking the doctorRepository to return the expectedDoctor
        when(doctorRepository.findById(validDoctorId)).thenReturn(Optional.of(doctor));

        // When
        Doctor result = doctorService.getDoctor(validDoctorId);

        // Then
        // Verify that findById is called with the correct doctorId
        then(doctorRepository).should(times(1)).findById(validDoctorId);

        // Verify that the returned result is the expectedDoctor
        assertThat(result).isEqualTo(doctor);

    }

    @Test
    void getDoctorWithInvalidId_ShouldThrowError() {

        // Given
        Long invalidDoctorId = 999L;
        // Mocking the doctorRepository to return an empty Optional (indicating not found)
        given(doctorRepository.findById(invalidDoctorId)).willThrow(ResourceNotFoundException.class);



        // Verify that a ResourceNotFoundException is thrown when the doctor is not found
        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctor(invalidDoctorId));

    }

    @Test
    void getAllDoctors_ShouldReturnNoDoctors() {

        // Given
        // Providing empty results from the doctorRepository
        given(doctorRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(Collections.emptyList()));

        Optional<String> specialization = Optional.of("Non-existent Specialization");
        Optional<String> department = Optional.of("Non-existent Department");
        Optional<String> name = Optional.of("Non-existent Name");
        Integer size = 10;
        Integer page = 0;

        // When
        Page<DoctorDTO> result = doctorService.getAllDoctors(specialization, department, name, size, page);

        // Then
        // Verify that findAll is called with the correct Specification and Pageable
        then(doctorRepository).should().findAll(any(Specification.class), any(Pageable.class));

        // Verify that the mapper's toDTO method is not called since the result is empty
        then(doctorMapper).should(times(0)).toDTO(any());

        // Verify that the returned result is empty
        assertThat(result).isEmpty();

    }

    @Test
    public void getAllDoctors_ShouldReturnListOfDoctors() {

        given(doctorRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(doctor)));

        // Given
        Optional<String> specialization = Optional.of("Cardiology");
        Optional<String> department = Optional.of("Hospital A");
        Optional<String> name = Optional.of("Dr. John");
        Integer size = 10;
        Integer page = 0;

        // When
        Page<DoctorDTO> result = doctorService.getAllDoctors(specialization, department, name, size, page);

        // Verify that findAll is called with the correct Specification and Pageable
        then(doctorRepository).should(times(1))
                .findAll(any(Specification.class), any(Pageable.class));

        // Verify that the mapper's toDTO method is called for the result Page
        then(doctorMapper).should().toDTO(any());

        // Verify that the returned result is empty (since we are using an empty page in the mock)
        assertThat(result).isNotEmpty();
    }

    @Test
    void saveDoctor() {

        given(doctorRepository.existsByEmail(anyString()))
                .willReturn(false);

        given(doctorMapper.toEntity(any(DoctorSignUp.class)))
                .willReturn(doctor);


        HttpStatus httpStatus = doctorService.saveDoctor(doctorSignUp);

        then(doctorRepository).should(times(1)).save(any(Doctor.class));
        assertThat(httpStatus).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void testSaveDoctor_DuplicateEmail_ThrowsResourceNotFoundException() {
        // Arrange
        DoctorSignUp doctorSignUp = new DoctorSignUp();
        // Set properties in doctorSignUp as needed

        given(doctorRepository.existsByEmail(doctorSignUp.getEmail())).willReturn(true);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> doctorService.saveDoctor(doctorSignUp));
    }
}