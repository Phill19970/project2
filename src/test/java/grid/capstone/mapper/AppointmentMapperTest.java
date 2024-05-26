package grid.capstone.mapper;

import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.model.Appointment;
import grid.capstone.model.Doctor;
import grid.capstone.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class AppointmentMapperTest {

    AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {

        appointmentMapper = AppointmentMapper.INSTANCE;

    }

    @Test
    public void testToDTO() {
        // Arrange
        AppointmentMapper appointmentMapper = new AppointmentMapperImpl();
        Doctor doctor = Doctor.builder().id(1L).name("Dr. Smith").build();
        Patient patient = Patient.builder().id(2L).name("John Doe").build();
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(LocalDate.of(2023, 7, 20))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .reason("Routine checkup")
                .build();

        // Act
        AppointmentDTO appointmentDTO = appointmentMapper.toDTO(appointment);

        // Assert
        assertEquals(1L, appointmentDTO.getDoctorId());
        assertEquals(2L, appointmentDTO.getPatientId());
        assertEquals(LocalDate.of(2023, 7, 20), appointmentDTO.getAppointmentDate());
        assertEquals(LocalTime.of(10, 0), appointmentDTO.getStartTime());
        assertEquals(LocalTime.of(11, 0), appointmentDTO.getEndTime());
        assertEquals("Routine checkup", appointmentDTO.getReason());
    }

    @Test
    public void testToEntity() {
        // Arrange
        AppointmentMapper appointmentMapper = new AppointmentMapperImpl();
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .doctorId(3L)
                .patientId(4L)
                .appointmentDate(LocalDate.of(2023, 7, 21))
                .startTime(LocalTime.of(15, 30))
                .endTime(LocalTime.of(16, 0))
                .reason("Follow-up appointment")
                .build();

        // Act
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        // Assert
        assertEquals(3L, appointment.getDoctor().getId());
        assertEquals(4L, appointment.getPatient().getId());
        assertEquals(LocalDate.of(2023, 7, 21), appointment.getAppointmentDate());
        assertEquals(LocalTime.of(15, 30), appointment.getStartTime());
        assertEquals(LocalTime.of(16, 0), appointment.getEndTime());
        assertEquals("Follow-up appointment", appointment.getReason());
    }

    @Test
    public void testToDTO_NullInput() {
        // Arrange
        AppointmentMapper appointmentMapper = new AppointmentMapperImpl();
        Appointment appointment = null;

        // Act
        AppointmentDTO appointmentDTO = appointmentMapper.toDTO(appointment);

        // Assert
        assertNull(appointmentDTO);
    }

    @Test
    public void testToEntity_NullInput() {

        // Arrange
        AppointmentMapper appointmentMapper = new AppointmentMapperImpl();
        AppointmentDTO appointmentDTO = null;

        // Act
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        // Assert
        assertNull(appointment);
    }
}