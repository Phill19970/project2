package grid.capstone.mapper;

import grid.capstone.dto.v1.ExpenseDTO;
import grid.capstone.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 22/06/2023
 */

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    Expense toEntity(ExpenseDTO expenseDTO);

    ExpenseDTO toDTO(Expense expense);


}
