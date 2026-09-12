package international_address_validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InternationalAddressValidationServiceTest {

    private InternationalAddressValidationService service;

    @BeforeEach
    public void setUp() {
        service = new InternationalAddressValidationService();
    }

    @Test
    public void testValidUSAddress() {
        Address address = new Address("123 Main St", "New York", "10001", "US");
        AddressValidationResult result = service.validateAddress("US", address);

        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(0, result.getViolations().size());
    }

    @Test
    public void testInvalidPostalCodeUSAddress() {
        Address address = new Address("123 Main St", "New York", "123", "US");
        AddressValidationResult result = service.validateAddress("US", address);

        assertFalse(result.isValid());
        assertTrue(result.getViolations().contains("Postal code too short"));
    }

    @Test
    public void testMissingCity() {
        Address address = new Address("123 Main St", "", "10001", "US");
        AddressValidationResult result = service.validateAddress("US", address);

        assertFalse(result.isValid());
        assertTrue(result.getViolations().contains("City must not be empty"));
    }

    @Test
    public void testMissingStreet() {
        Address address = new Address("", "New York", "10001", "US");
        AddressValidationResult result = service.validateAddress("US", address);

        assertFalse(result.isValid());
        assertTrue(result.getViolations().contains("Street must not be empty"));
    }

    @Test
    public void testUnsupportedCountry() {
        Address address = new Address("123 Main St", "Tokyo", "100-0001", "JP");

        assertThrows(RuntimeException.class, () -> {
            service.validateAddress("JP", address);
        });
    }

    @Test
    public void testGermanAddress() {
        Address address = new Address("Hauptstrasse 1", "Berlin", "10115", "DE");
        AddressValidationResult result = service.validateAddress("DE", address);

        assertTrue(result.isValid());
    }

    @Test
    public void testCanadianAddress() {
        Address address = new Address("123 Main St", "Toronto", "M5V 3A8", "CA");
        AddressValidationResult result = service.validateAddress("CA", address);

        assertTrue(result.isValid());
    }

    @Test
    public void testNullAddress() {
        // This test exposes flaw #6 - null check doesn't return
        assertThrows(Exception.class, () -> {
            service.validateAddress("US", null);
        });
    }

    @Test
    public void testNullPostalCode() {
        // This test exposes flaw #8 - no null check before method call
        Address address = new Address("123 Main St", "New York", null, "US");
        assertThrows(Exception.class, () -> {
            service.validateAddress("US", address);
        });
    }

    @Test
    public void testSerializationOfResult() {
        Address address = new Address("123 Main St", "New York", "10001", "US");
        AddressValidationResult result = service.validateAddress("US", address);

        // This will fail if getters are missing (flaw #2)
        assertNotNull(result.isValid());
        assertNotNull(result.getViolations());
    }

    @Test
    public void testResultViolationCount() {
        AddressValidationResult result = new AddressValidationResult();
        result.addViolation("CRITICAL");
        result.addViolation("CRITICAL");

        // This test exposes flaw #13 - getViolationCount uses == instead of equals()
        assertEquals(2, result.getViolationCount());
    }
}

