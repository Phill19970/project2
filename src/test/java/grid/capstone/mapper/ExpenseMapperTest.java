package grid.capstone.mapper;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExpenseMapperTest {

    ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        expenseMapper = ExpenseMapper.INSTANCE;
    }

    @Test
    public void testToEntity() {
        // Arrange
        ExpenseDTO expenseDTO = ExpenseDTO.builder()
                .name("Electricity Bill")
                .category("Utilities")
                .description("Monthly electricity bill payment")
                .amount(BigDecimal.valueOf(100.0))
                .build();

        // Act
        Expense expense = expenseMapper.toEntity(expenseDTO);

        // Assert
        assertEquals("Electricity Bill", expense.getName());
        assertEquals("Utilities", expense.getCategory());
        assertEquals("Monthly electricity bill payment", expense.getDescription());
        assertEquals(BigDecimal.valueOf(100.0), expense.getAmount());
    }

    @Test
    public void testToDTO() {
        // Arrange
        Expense expense = Expense.builder()
                .name("Groceries")
                .category("Food")
                .description("Weekly grocery shopping")
                .amount(BigDecimal.valueOf(50.0))
                .build();

        // Act
        ExpenseDTO expenseDTO = expenseMapper.toDTO(expense);

        // Assert
        assertEquals("Groceries", expenseDTO.getName());
        assertEquals("Food", expenseDTO.getCategory());
        assertEquals("Weekly grocery shopping", expenseDTO.getDescription());
        assertEquals(BigDecimal.valueOf(50.0), expenseDTO.getAmount());
    }

    @Test
    public void testToEntity_NullInput() {
        // Arrange
        ExpenseDTO expenseDTO = null;

        // Act
        Expense expense = expenseMapper.toEntity(expenseDTO);

        // Assert
        assertNull(expense);
    }

    @Test
    public void testToDTO_NullInput() {
        // Arrange
        Expense expense = null;

        // Act
        ExpenseDTO expenseDTO = expenseMapper.toDTO(expense);

        // Assert
        assertNull(expenseDTO);
    }

}