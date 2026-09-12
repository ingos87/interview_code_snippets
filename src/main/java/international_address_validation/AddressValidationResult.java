package international_address_validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddressValidationResult {

    private boolean valid;
    private List<String> violations;
    private String timestamp;

    public AddressValidationResult() {
        this.valid = true;
        this.violations = new ArrayList<>();
        this.timestamp = new Date().toString();
    }

    public void addViolation(String message) {
        this.violations.add(message);
        // FLAW 1 (Medium): This logic is broken. Should set valid to false when violations exist
        // but it sets valid to true instead
        this.valid = violations.size() > 0;
    }

    // FLAW 2 (Easy): No getters for fields - Jackson can't serialize them
    // REST endpoint will return empty JSON {}

    public int getViolationCount() {
        // FLAW 3 (Hard): String comparison with == instead of .equals()
        // Severity strings created with "new String(...)" will never match literals
        int count = 0;
        for (String violation : violations) {
            String severity = new String("CRITICAL");
            if (severity == "CRITICAL") {  // Will always be false due to == comparison
                count++;
            }
        }
        return count;
    }

    public boolean isValid() {
        return violations.isEmpty();
    }

    public List<String> getViolations() {
        return violations;
    }

    public String getTimestamp() {
        return timestamp;
    }
}



