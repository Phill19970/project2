package grid.capstone.util;

import java.util.function.Consumer;

/**
 * @author Javaughn Stephenson
 * @since 28/06/2023
 */

public class UpdateUtil {

    public static <T> void updateHelper(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
