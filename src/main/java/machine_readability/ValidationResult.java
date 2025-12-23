package machine_readability;

import java.util.List;

public class ValidationResult {

    private boolean valid;
    private List<String> errors;

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors;
    }

}
