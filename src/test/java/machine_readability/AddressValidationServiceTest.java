package machine_readability;

import org.junit.jupiter.api.Test;

class AddressValidationServiceTest {

    private AddressValidationService service = new AddressValidationService();

    @Test
    void validateSimple() {
        Address adr = new Address("Mr Foo", "Bar Street", "42c", "12345", "Hamburg", "DE");

        ValidationResult result = service.validate(adr);

        assert result != null;
    }
}