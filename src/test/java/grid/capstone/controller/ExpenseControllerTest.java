package grid.capstone.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.filter.JwtTokenFilter;
import grid.capstone.model.Expense;
import grid.capstone.service.expense.ExpenseService;
import grid.capstone.service.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExpenseController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    static final String BASE_URL = "/api/v1/expenses";


    @MockBean
    JwtService jwtService;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ExpenseService expenseService;
    Expense expense;

    ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {

        expense = Expense.builder()
                .id(1L)
                .build();

        expenseDTO = ExpenseDTO.builder()
                .amount(BigDecimal.valueOf(123))
                .name("Prescription")
                .description("Description")
                .category("Prescription")
                .build();

    }

    @Test
    void getPatientExpenses() throws Exception {

        List<Expense> expectedExpense = List.of(expense);

        given(expenseService.getPatientExpenses(anyLong()))
                .willReturn(expectedExpense);

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL)
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk());

    }

    @Test
    void createExpense() throws Exception {

        given(expenseService.createExpense(anyLong(), any(ExpenseDTO.class)))
                .willReturn(HttpStatus.CREATED);

        ResultActions resultActions = mockMvc.perform(
                post(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO))
        );


        resultActions
                .andExpect(status().isCreated());


    }

    @Test
    void getExpense() throws Exception {

        given(expenseService.getExpense(anyLong()))
                .willReturn(expense);

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URL + "/1")
        );

        resultActions
                .andExpect(status().isOk());


        String content = resultActions.andReturn().getResponse().getContentAsString();
        Expense read = objectMapper.readValue(content, new TypeReference<Expense>() {
        });

        assertThat(read).isEqualTo(expense);
    }

    @Test
    void updateExpense() throws Exception {

        given(expenseService.updateExpense(anyLong(), any(Expense.class)))
                .willReturn(HttpStatus.OK);

        ResultActions resultActions = mockMvc.perform(
                put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense))
        );

        resultActions
                .andExpect(status().isOk());

    }
}