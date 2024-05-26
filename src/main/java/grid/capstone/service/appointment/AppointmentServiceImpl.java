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
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;


    @Override
    public HttpStatus createAppointment(AppointmentDTO appointmentDTO) {

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + appointmentDTO.getPatientId() + "is not found"));

        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + appointmentDTO.getDoctorId() + "is not found"));

        /*
        If any appointment has a conflict then return conflict
         */
        if (Boolean.TRUE.equals(hasAppointmentConflict(appointment))){
            throw new AppointmentConflictException("Appointment has conflict");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        emailService.sendAppointmentEmail(
                savedAppointment,
                doctor,
                patient
        );

        return HttpStatus.CREATED;
    }


    @Override
    public List<Appointment> getFilteredAppointments(Optional<LocalDate> dateFilter, Optional<Long> patientId, Optional<Long> doctorId) {
        if (patientId.isEmpty() && doctorId.isEmpty()) {
            throw new ResourceNotFoundException("No patient or doctor id specified");
        }

        //Add the date filter if client added in req param
        Specification<Appointment> appointmentSpecification =
                Specification
                        .where(dateFilter
                                .map(AppointmentSpecification::hasDate)
                                .orElse(null)
                        )
                        .and(doctorId
                                .map(AppointmentSpecification::hasDoctor)
                                .orElse(null)
                        )
                        .and(patientId
                                .map(AppointmentSpecification::hasPatient)
                                .orElse(null)
                        );

        return appointmentRepository.findAll(appointmentSpecification);
    }


    @Override
    public HttpStatus updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {

        Appointment updatedAppointment = appointmentMapper.toEntity(appointmentDTO);


        //TODO: Add exception handling when id is not found
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + appointmentId + " does not exist"));


        //Update Values in the object if not null
        appointment.updateObject(updatedAppointment);

        if (Boolean.TRUE.equals(hasAppointmentConflict(appointment))){
            throw new AppointmentConflictException("Appointment has conflict");
        }


        appointmentRepository.save(appointment);

        emailService.sendAppointmentEmail(
                appointment,
                appointment.getDoctor(),
                appointment.getPatient()
        );

        return HttpStatus.OK;
    }


    private Boolean hasAppointmentConflict(Appointment appointment) {

        List<Appointment> patientAppointments = appointmentRepository.findByPatient(appointment.getPatient());
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctor(appointment.getDoctor());


        //Check if any existing appointments is clashing with the new one
        boolean patientMatch = patientAppointments.stream()
                .filter(app -> !Objects.equals(app.getId(), appointment.getId()))
                .filter(app -> app.getAppointmentDate().equals(appointment.getAppointmentDate()))
                .anyMatch(app -> isAppointmentClashing(app, appointment));

        boolean doctorMatch = doctorAppointments.stream()
                .filter(app -> !Objects.equals(app.getId(), appointment.getId()))
                .filter(app -> app.getAppointmentDate().equals(appointment.getAppointmentDate()))
                .anyMatch(app -> isAppointmentClashing(app, appointment));

        /*
        If any is true, there's a conflict in the appointment
        Only return false when both are false.
         */
        return patientMatch || doctorMatch;
    }

    //Checks to see if any conflict between two given appointments
    private Boolean isAppointmentClashing(Appointment appointment1, Appointment appointment2) {
        return appointment1.getStartTime().isBefore(appointment2.getEndTime())
                && appointment2.getStartTime().isBefore(appointment1.getEndTime());
    }

}
