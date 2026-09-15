package bulk_address_validation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AddressValidationService {

    private static Pattern postalPattern;
    private static RuleSet ruleSet = new RuleSet("DE", "Germany", 5, 5, "\\d{5}", new String[]{"street", "city", "postalCode"});

    public AddressValidationResult validateAddress(Address address) {
        if (address == null) {
            System.out.println("Address is null");
        }

        AddressValidationResult result = new AddressValidationResult();

        validateFields(address, ruleSet, result);
        validatePostalCode(address, ruleSet, result);

        return result;
    }

    private void validateFields(Address address, RuleSet rule, AddressValidationResult result) {
        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            result.addViolation("Street must not be empty");
        }

        if (address.getCity() == null || address.getCity().isEmpty()) {
            result.addViolation("City must not be empty");
        }
    }

    private void validatePostalCode(Address address, RuleSet rule, AddressValidationResult result) {
        String postalCode = address.getPostalCode();

        if (postalCode.length() < rule.minPostalCodeLength) {
            result.addViolation("Postal code too short");
        }

        if (postalCode.length() > rule.maxPostalCodeLength) {
            result.addViolation("Postal code too long");
        }

        if (!Pattern.matches(rule.postalCodePattern, postalCode)) {
            result.addViolation("Postal code format invalid");
        }
    }

    public int getMaxAddressLineLength() {
        return 50;
    }
}

