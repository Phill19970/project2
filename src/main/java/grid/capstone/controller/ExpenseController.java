package grid.capstone.controller;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.model.Expense;
import grid.capstone.service.expense.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 22/06/2023
 */

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getPatientExpenses(@RequestParam(required = true) Long patientId) {
        return expenseService.getPatientExpenses(patientId);
    }

    @PostMapping("/{patientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> createExpense(
            @PathVariable Long patientId,
            @Valid @RequestBody ExpenseDTO expenseDTO
    ) {
        return ResponseEntity
                .status(expenseService.createExpense(patientId, expenseDTO))
                .build();
    }

    @GetMapping("/{expenseId}")
    public Expense getExpense(@PathVariable Long expenseId) {
        return expenseService.getExpense(expenseId);
    }

    @PutMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody Expense expense
    ) {
        return ResponseEntity
                .status(expenseService.updateExpense(expenseId, expense))
                .build();
    }

}
