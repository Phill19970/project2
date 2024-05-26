package grid.capstone.service.expense;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.ExpenseMapper;
import grid.capstone.model.Expense;
import grid.capstone.model.Patient;
import grid.capstone.repository.ExpenseRepository;
import grid.capstone.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 22/06/2023
 */

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;
    private final PatientRepository patientRepository;


    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper, PatientRepository patientRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Expense> getPatientExpenses(Long patientId) {
        //TODO: Throw exception when id is not found
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient with id " + patientId + " does not exist");
        }


        return expenseRepository.findAllByPatientId(
                patientId
        );
    }

    @Override
    public HttpStatus createExpense(Long patientId, ExpenseDTO expenseDTO) {
        //TODO: Throw exception if patient doesnt exist
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient with id " + patientId + " does not exist");
        }

        Expense postExpense = expenseMapper.toEntity(expenseDTO);

        postExpense.setDateOfExpense(LocalDate.now());
        postExpense.setPaid(false);


        postExpense.setPatient(
                Patient.builder().id(patientId).build()
        );

        expenseRepository.save(postExpense);

        return HttpStatus.CREATED;
    }

    @Override
    public Expense getExpense(Long expenseId) {
        //TODO: Throw exception when id isnt found
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id " + expenseId + " does not exist"));
    }

    @Override
    public HttpStatus updateExpense(Long expenseId, Expense updatedExpense) {
        //TODO: Throw exception when id not found
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id " + expenseId + " does not exist"));

        expense.updateObject(updatedExpense);
        expenseRepository.save(expense);

        return HttpStatus.OK;
    }
}
