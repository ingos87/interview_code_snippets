package bulk_address_validation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalAddressValidationSystemImpl implements ExternalAddressValidationSystem {

    private static final double CONFIDENCE_THRESHOLD = 0.75;
    private static final boolean SYSTEM_AVAILABLE = true;

    @Override
    public List<ExternalValidationResult> validateBulk(List<Address> addresses) {
        List<ExternalValidationResult> results = new ArrayList<>();

        if (!isSystemAvailable()) {
            throw new RuntimeException("External validation system is unavailable");
        }

        if (addresses == null || addresses.isEmpty()) {
            return results;
        }

        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            String addressId = "addr_" + i;

            List<String> addressLines = new ArrayList<>();
            addressLines.add(address.getStreet());
            addressLines.add(address.getCity());
            addressLines.add(address.getPostalCode());

            ExternalValidationResult result = validateSingleAddressLines(addressId, addressLines);
            results.add(result);
        }

        return results;
    }

    private ExternalValidationResult validateSingleAddressLines(String addressId, List<String> addressLines) {
        // Calculate confidence based on address completeness and format
        double confidence = calculateConfidence(addressLines);

        return new ExternalValidationResult(
            addressId,
            addressLines,
            confidence
        );
    }

    private double calculateConfidence(List<String> addressLines) {
        if (addressLines == null || addressLines.isEmpty()) {
            return 0.0;
        }

        double confidence = 0.5; // Base confidence

        // Check street
        if (addressLines.size() > 0 && addressLines.get(0) != null && !addressLines.get(0).isEmpty()) {
            String street = addressLines.get(0);
            if (street.length() >= 5 && street.matches(".*[0-9].*")) { // Contains number
                confidence += 0.2;
            } else if (street.length() >= 3) {
                confidence += 0.1;
            }
        }

        // Check city
        if (addressLines.size() > 1 && addressLines.get(1) != null && !addressLines.get(1).isEmpty()) {
            String city = addressLines.get(1);
            if (city.length() >= 3 && city.matches("[A-Za-z\\s]+")) { // Only letters and spaces
                confidence += 0.2;
            } else if (city.length() >= 2) {
                confidence += 0.1;
            }
        }

        // Check postal code
        if (addressLines.size() > 2 && addressLines.get(2) != null && !addressLines.get(2).isEmpty()) {
            String postalCode = addressLines.get(2);
            if (postalCode.matches("\\d{5}")) { // Standard 5-digit postal code
                confidence += 0.2;
            } else if (postalCode.matches("\\d{3,}")) { // At least 3 digits
                confidence += 0.1;
            }
        }

        // Cap confidence at 1.0
        return Math.min(confidence, 1.0);
    }

    @Override
    public boolean isSystemAvailable() {
        return SYSTEM_AVAILABLE;
    }

    @Override
    public double getConfidenceThreshold() {
        return CONFIDENCE_THRESHOLD;
    }
}


