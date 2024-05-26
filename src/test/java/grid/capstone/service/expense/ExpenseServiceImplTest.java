package grid.capstone.service.expense;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.exception.ResourceNotFoundException;
import grid.capstone.mapper.ExpenseMapper;
import grid.capstone.mapper.ExpenseMapperImpl;
import grid.capstone.model.Expense;
import grid.capstone.model.Patient;
import grid.capstone.repository.ExpenseRepository;
import grid.capstone.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    ExpenseRepository expenseRepository;
    @Mock
    PatientRepository patientRepository;

    ExpenseMapper expenseMapper = new ExpenseMapperImpl();
    ExpenseService expenseService;

    Patient testPatient;
    ExpenseDTO testExpenseDTO;
    Expense testExpense;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseMapper, patientRepository);

        testPatient = Patient.builder()
                .name("test")
                .email("test@email.com")
                .build();

        testExpenseDTO = ExpenseDTO.builder()
                .name("Test Expense")
                .category("Test")
                .amount(new BigDecimal("100.00"))
                .build();

        testExpense = Expense.builder()
                .name("Expense")
                .build();
    }

    @Test
    void getPatientExpenses() {
        given(patientRepository.existsById(anyLong())).willReturn(true);
        given(expenseRepository.findAllByPatientId(anyLong())).willReturn(Arrays.asList(testExpense));

        //when
        List<Expense> patientExpenses = expenseService.getPatientExpenses(1L);

        //then
        then(patientRepository).should(times(1)).existsById(1L);
        then(expenseRepository).should(times(1)).findAllByPatientId(1L);
        assertThat(patientExpenses).isEqualTo(Arrays.asList(testExpense));
    }

    @Test
    void getPatientExpenses_GivenWrongId_ThrowResourceError() {
        //given
        given(patientRepository.existsById(anyLong())).willReturn(false);


        //then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.getPatientExpenses(1L));
        then(patientRepository).should().existsById(1L);
    }

    @Test
    void createExpense() {
        given(patientRepository.existsById(anyLong())).willReturn(true);

        //when
        HttpStatus httpStatus = expenseService.createExpense(1L, testExpenseDTO);


        then(patientRepository).should(times(1)).existsById(1L);
        then(expenseRepository).should(times(1)).save(any(Expense.class));
        assertThat(httpStatus).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createExpense_GivenWrongId_ThrowResourceError() {
        //given
        given(patientRepository.existsById(anyLong())).willReturn(false);


        //then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.createExpense(1L, testExpenseDTO));
        then(patientRepository).should().existsById(1L);
    }

    @Test
    void getExpense() {
        given(expenseRepository.findById(anyLong())).willReturn(Optional.of(testExpense));

        Expense expense = expenseService.getExpense(1L);

        then(expenseRepository).should(times(1)).findById(1L);
        assertThat(expense).isEqualTo(testExpense);
    }

    @Test
    void getExpense_GivenWrongId_ThrowResourceError() {
        //given
        given(expenseRepository.findById(anyLong())).willThrow(ResourceNotFoundException.class);


        //then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpense(1L));
        then(expenseRepository).should(times(1)).findById(1L);
    }

    @Test
    void updateExpense() {
        Expense updateExpense = Expense.builder().build();

        given(expenseRepository.findById(anyLong())).willReturn(Optional.of(testExpense));

        HttpStatus httpStatus = expenseService.updateExpense(1L, updateExpense);

        then(expenseRepository).should(times(1)).save(any(Expense.class));
        then(expenseRepository).should(times(1)).findById(1L);
        assertThat(httpStatus).isEqualTo(HttpStatus.OK);
    }
}