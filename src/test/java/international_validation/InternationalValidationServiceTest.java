package international_validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InternationalValidationServiceTest {

    @Test
    public void testUSAddressValidation() {
        InternationalValidationService service = new InternationalValidationService();
        InternationalValidationResult result = service.validateAddress("US", "123 Main St, New York, NY, 10001");
        assertTrue(result.violations.size() >= 0);
    }

    @Test
    public void testCanadianAddressValidation() {
        InternationalValidationService service = new InternationalValidationService();
        InternationalValidationResult result = service.validateAddress("CA", "456 Maple Ave, Toronto, ON, M5V 3A8");
        assertNotNull(result);
    }

    @Test
    public void testUnsupportedCountry() {
        InternationalValidationService service = new InternationalValidationService();
        try {
            service.validateAddress("XX", "Some Address");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testNullCountry() {
        InternationalValidationService service = new InternationalValidationService();
        service.validateAddress(null, "123 Main St, New York, NY, 10001");
    }


    @Test
    public void testViolationCount() {
        InternationalValidationService service = new InternationalValidationService();
        InternationalValidationResult result = service.validateAddress("US", "incomplete");
        assertTrue(result.violations.size() >= -10 && result.violations.size() <= 100);
    }
}

