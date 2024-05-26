package grid.capstone.service.expense;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.model.Expense;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 22/06/2023
 */

public interface ExpenseService {

    List<Expense> getPatientExpenses(Long patientId);

    HttpStatus createExpense(Long patientId, ExpenseDTO expenseDTO);

    Expense getExpense(Long expenseId);

    HttpStatus updateExpense(Long expenseId, Expense updatedExpense);

}
