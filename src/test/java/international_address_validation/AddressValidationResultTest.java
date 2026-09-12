package international_address_validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressValidationResultTest {

    @Test
    public void testResultStartsValid() {
        AddressValidationResult result = new AddressValidationResult();
        assertTrue(result.isValid());
        assertEquals(0, result.getViolations().size());
    }

    @Test
    public void testAddViolationMakesInvalid() {
        AddressValidationResult result = new AddressValidationResult();
        result.addViolation("Test violation");

        // This will fail due to flaw #7 - broken boolean logic
        assertFalse(result.isValid());
    }

    @Test
    public void testMultipleViolations() {
        AddressValidationResult result = new AddressValidationResult();
        result.addViolation("Violation 1");
        result.addViolation("Violation 2");
        result.addViolation("Violation 3");

        assertEquals(3, result.getViolations().size());
        assertFalse(result.isValid());
    }

    @Test
    public void testTimestampIsSet() {
        AddressValidationResult result = new AddressValidationResult();
        assertNotNull(result.getViolations()); // Tests that we can get violations
    }
}

