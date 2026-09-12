package international_address_validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Response DTO for address validation.
 * Provides proper JSON serialization with getters for Jackson.
 */
public class AddressValidationResponse {

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("violations")
    private List<String> violations;

    @JsonProperty("timestamp")
    private String timestamp;

    public AddressValidationResponse() {
    }

    public AddressValidationResponse(boolean valid, List<String> violations, String timestamp) {
        this.valid = valid;
        this.violations = violations;
        this.timestamp = timestamp;
    }

    public static AddressValidationResponse fromResult(AddressValidationResult result) {
        return new AddressValidationResponse(
                result.isValid(),
                result.getViolations(),
                result.getTimestamp()
        );
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressValidationResponse that = (AddressValidationResponse) o;
        return valid == that.valid &&
                Objects.equals(violations, that.violations) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid, violations, timestamp);
    }

    @Override
    public String toString() {
        return "AddressValidationResponse{" +
                "valid=" + valid +
                ", violations=" + violations +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

