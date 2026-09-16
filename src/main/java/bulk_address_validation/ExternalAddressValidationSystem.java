package bulk_address_validation;

import java.util.List;
import java.util.Map;

/**
 * Interface for external address validation system.
 * This system validates whether addresses really exist by calling an external service.
 * It processes bulk addresses only (no single address processing).
 */
public interface ExternalAddressValidationSystem {

    /**
     * Validates addresses in bulk format where each address is represented as separate fields.
     *
     * @param addresses List of Address objects to validate
     * @return List of ExternalValidationResult with confidence values
     */
    List<ExternalValidationResult> validateBulk(List<Address> addresses);

    /**
     * Checks if the system is available for bulk processing.
     *
     * @return true if system is operational, false otherwise
     */
    boolean isSystemAvailable();

    /**
     * Gets the minimum confidence threshold for valid addresses.
     *
     * @return confidence threshold value between 0.0 and 1.0
     */
    double getConfidenceThreshold();
}

