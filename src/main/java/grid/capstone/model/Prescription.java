package grid.capstone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static grid.capstone.util.UpdateUtil.updateHelper;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medication;

    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @Future(message = "End Date Should be in the future")
    private LocalDate endDate;
    private Integer dosage;
    private BigDecimal total;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "medical_record_id")
    @JsonIgnore
    private MedicalRecord medicalRecord;


    public void updateObject(Prescription prescription) {
        updateHelper(prescription.getMedication(), this::setMedication);
        updateHelper(prescription.getStartDate(), this::setStartDate);
        updateHelper(prescription.getEndDate(), this::setEndDate);
        updateHelper(prescription.getDosage(), this::setDosage);
        updateHelper(prescription.getTotal(), this::setTotal);
    }

}
