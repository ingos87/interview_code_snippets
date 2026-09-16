package bulk_address_validation;

import java.util.List;
import java.util.Objects;

/**
 * Represents the result from external address validation system.
 * Contains confidence values for validated addresses.
 */
public class ExternalValidationResult {

    private String addressId;
    private List<String> addressLines;
    private double confidenceValue;

    public ExternalValidationResult() {
    }

    public ExternalValidationResult(String addressId, List<String> addressLines,
                                    double confidenceValue) {
        this.addressId = addressId;
        this.addressLines = addressLines;
        this.confidenceValue = confidenceValue;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    public double getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(double confidenceValue) {
        this.confidenceValue = confidenceValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalValidationResult that = (ExternalValidationResult) o;
        return Double.compare(that.confidenceValue, confidenceValue) == 0 &&
               Objects.equals(addressId, that.addressId) &&
               Objects.equals(addressLines, that.addressLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, addressLines, confidenceValue);
    }

    @Override
    public String toString() {
        return "ExternalValidationResult{" +
                "addressId='" + addressId + '\'' +
                ", addressLines=" + addressLines +
                ", confidenceValue=" + confidenceValue +
                '}';
    }
}

