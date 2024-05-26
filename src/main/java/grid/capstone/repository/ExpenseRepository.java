package grid.capstone.repository;

import grid.capstone.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByPatientId(Long patientId);
}
