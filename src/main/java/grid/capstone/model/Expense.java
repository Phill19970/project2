package grid.capstone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of expense is required")
    private String name;
    private String category;
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;

    @NotNull(message = "Date of expense is required")
    private LocalDate dateOfExpense;
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;


    public void updateObject(Expense expense) {
        updateHelper(expense.getName(), this::setName);
        updateHelper(expense.getCategory(), this::setCategory);
        updateHelper(expense.getDescription(), this::setDescription);
        updateHelper(expense.getAmount(), this::setAmount);
        updateHelper(expense.getPaid(), this::setPaid);
    }


}
