package grid.capstone.exception;

/**
 * @author Javaughn Stephenson
 * @since 11/07/2023
 */

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
