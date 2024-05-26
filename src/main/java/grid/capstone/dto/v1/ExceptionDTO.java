package grid.capstone.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

/**
 * @author Javaughn Stephenson
 * @since 11/07/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionDTO {

    private HttpStatus status;
    private String message;
    private String details;
    private LocalTime timestamp;

}