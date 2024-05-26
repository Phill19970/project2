package grid.capstone.service.appointment;

import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.exception.AppointmentConflictException;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.AppointmentMapper;
import grid.capstone.model.Appointment;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import grid.capstone.repository.AppointmentRepository;
import grid.capstone.repository.DoctorRepository;
import grid.capstone.repository.PatientRepository;
import grid.capstone.service.mail.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    AppointmentService appointmentService;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    PatientRepository patientRepository;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    EmailService emailService;

    Appointment appointment;

    AppointmentDTO appointmentDTO;

    Patient patient;

    Doctor doctor;


    @BeforeEach
    void setUp() {

        appointmentService = new AppointmentServiceImpl(appointmentMapper, patientRepository, doctorRepository, appointmentRepository, emailService);


        appointmentDTO = AppointmentDTO.builder()
                .doctorId(1L)
                .patientId(1L)
                .build();

        patient = Patient.builder()
                .id(1L)
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .build();

        appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .build();

    }

    @Test
    void createAppointment() {

        // Given

        // Stubbing the AppointmentMapper's toEntity method to return an Appointment instance
        given(appointmentMapper.toEntity(any(AppointmentDTO.class))).willReturn(appointment);

        // Stubbing the PatientRepository to return a patient when findById is called
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));

        // Stubbing the DoctorRepository to return a doctor given() findById is called
        given(doctorRepository.findById(1L)).willReturn(Optional.of(doctor));

        // Stubbing the AppointmentRepository to return the saved appointment given() save is called
        given(appointmentRepository.save(appointment)).willReturn(appointment);

        given(appointmentRepository.findByDoctor(any(Doctor.class)))
                .willReturn(List.of());


        given(appointmentRepository.findByPatient(any(Patient.class)))
                .willReturn(List.of());


        // When
        HttpStatus result = appointmentService.createAppointment(appointmentDTO);

        // Then
        then(patientRepository).should(times(1)).findById(1L);

        then(doctorRepository).should(times(1)).findById(1L);

        then(appointmentRepository).should().save(appointment);

        then(emailService).should().sendAppointmentEmail(appointment, doctor, patient);

        assertThat(result).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    void getFilteredAppointments() {

        // Given
        Optional<LocalDate> dateFilter = Optional.of(LocalDate.of(2023, 7, 20));
        Optional<Long> patientId = Optional.of(1L);
        Optional<Long> doctorId = Optional.of(1L);
        List<Appointment> expectedAppointments = Collections.singletonList(new Appointment());

        // Stubbing the AppointmentRepository to return expectedAppointments when findAll is called
        given(appointmentRepository.findAll(any(Specification.class)))
                .willReturn(expectedAppointments);

        // When
        List<Appointment> result = appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId);

        // Then
        // Verify that findAll is called on the appointmentRepository with the correct Specification
        then(appointmentRepository).should().findAll(any(Specification.class));
        // Verify that the result matches the expectedAppointments
        assertThat(result).isEqualTo(expectedAppointments);

    }

    @Test
    public void testGetFilteredAppointmentsWithEmptyPatientAndDoctorIds() {
        // Given
        Optional<LocalDate> dateFilter = Optional.of(LocalDate.of(2023, 7, 20));
        Optional<Long> patientId = Optional.empty();
        Optional<Long> doctorId = Optional.empty();

        // When and Then
        assertThrows(ResourceNotFoundException.class, () -> appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId));
        // Verify that findAll is not called on the appointmentRepository since no patient or doctor id is specified
        then(appointmentRepository).should(never()).findAll(any(Specification.class));
    }

    @Test
    void updateAppointment() {

        // Given
        Long appointmentId = 1L;

        // Stubbing the AppointmentMapper's toEntity method to return an updated appointment
        Appointment updatedAppointment = Appointment.builder()
                .appointmentDate(LocalDate.now().plusDays(5))
                .build();


        given(appointmentMapper.toEntity(appointmentDTO))
                .willReturn(updatedAppointment);

        // Stubbing the AppointmentRepository to return an existing appointment when findById is called
        given(appointmentRepository.findById(appointmentId))
                .willReturn(Optional.of(appointment));

        // Stubbing the appointmentRepository save method to return the saved appointment
        given(appointmentRepository.save(any(Appointment.class)))
                .willReturn(appointment);

        given(appointmentRepository.findByDoctor(any(Doctor.class)))
                .willReturn(List.of());


        given(appointmentRepository.findByPatient(any(Patient.class)))
                .willReturn(List.of());

        // When
        HttpStatus result = appointmentService.updateAppointment(appointmentId, appointmentDTO);

        // Then
        // Verify that findById is called with the correct appointmentId
        then(appointmentRepository).should().findById(appointmentId);

        // Verify that the appointmentMapper's toEntity method is called with the correct appointmentDTO
        then(appointmentMapper).should().toEntity(appointmentDTO);

        // Verify that the appointment is saved in the repository
        then(appointmentRepository).should().save(any(Appointment.class));

        // Verify that the emailService.sendAppointmentEmail method is called
        then(emailService).should().sendAppointmentEmail(any(Appointment.class), any(Doctor.class), any(Patient.class));

        // Verify that HttpStatus.OK is returned
        assertThat(result).isEqualTo(HttpStatus.OK);
    }



    @Test
    void testCreateAppointment_AppointmentClashes_ThrowsAppointmentConflictException() {
        //Arrange
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setDoctorId(2L);
        appointmentDTO.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointmentDTO.setStartTime(LocalTime.of(10, 0));
        appointmentDTO.setEndTime(LocalTime.of(11, 0));

        Patient patient = new Patient();
        patient.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(2L);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointment.setStartTime(LocalTime.of(9, 0));
        appointment.setEndTime(LocalTime.of(10, 30));

        Appointment appointment1 = new Appointment();
        appointment1.setId(2L);
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        appointment1.setAppointmentDate(LocalDate.of(2023, 7, 25));
        appointment1.setStartTime(LocalTime.of(9, 0));
        appointment1.setEndTime(LocalTime.of(10, 30));

        List<Appointment> patientAppointments = new ArrayList<>();
        patientAppointments.add(appointment1);

        List<Appointment> doctorAppointments = new ArrayList<>();
        doctorAppointments.add(appointment1);

        given(appointmentMapper.toEntity(appointmentDTO)).willReturn(appointment);
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(doctorRepository.findById(2L)).willReturn(Optional.of(doctor));
        given(appointmentRepository.findByPatient(patient)).willReturn(patientAppointments);
        given(appointmentRepository.findByDoctor(doctor)).willReturn(doctorAppointments);

        // Act & Assert
        assertThrows(AppointmentConflictException.class, () -> {
            appointmentService.createAppointment(appointmentDTO);
        });
    }


}