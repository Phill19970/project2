package grid.capstone.controller;

import grid.capstone.dto.v1.DoctorDTO;
import grid.capstone.dto.v1.DoctorSignUp;
import grid.capstone.model.Doctor;
import grid.capstone.service.doctor.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Validated
@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    Page<DoctorDTO> getAllDoctors(
            @RequestParam Optional<String> specialization,
            @RequestParam Optional<String> department,
            @RequestParam Optional<String> name,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page
            ) {
        return doctorService.getAllDoctors(
                specialization, department, name, size, page
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus saveDoctor(@RequestBody DoctorSignUp doctorSignUp) {

        return doctorService.saveDoctor(doctorSignUp);

    }

    @GetMapping("/{doctorId}")
    public Doctor getDoctor(@PathVariable Long doctorId) {
        return doctorService.getDoctor(doctorId);
    }



}
