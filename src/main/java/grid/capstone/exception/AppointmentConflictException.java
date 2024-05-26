package grid.capstone.exception;

/**
 * @author Javaughn Stephenson
 * @since 11/07/2023
 */

public class AppointmentConflictException extends RuntimeException{

    public AppointmentConflictException(String message) {
        super(message);
    }

}
