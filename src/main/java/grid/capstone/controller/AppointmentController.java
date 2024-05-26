package grid.capstone.controller;

import grid.capstone.dto.v1.AppointmentDTO;
import grid.capstone.model.Appointment;
import grid.capstone.service.appointment.AppointmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 20/06/2023
 */

@Validated
@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {

        log.info("Appointment = {}", appointmentDTO);

        return ResponseEntity
                .status(appointmentService.createAppointment(appointmentDTO))
                .build();
    }

    @GetMapping
    public List<Appointment> getFilteredAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate>  dateFilter,
            @RequestParam Optional<Long> patientId,
            @RequestParam Optional<Long> doctorId
    ) {
        return appointmentService.getFilteredAppointments(dateFilter, patientId, doctorId);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<HttpStatus> updateAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentDTO appointmentDTO
    ) {
        return ResponseEntity
                .status(appointmentService.updateAppointment(appointmentId, appointmentDTO))
                .build();
    }

}
