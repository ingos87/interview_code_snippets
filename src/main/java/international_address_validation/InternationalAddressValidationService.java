package international_address_validation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class InternationalAddressValidationService {

    private static Map<String, CountryRule> rules = new HashMap<>();
    private static Pattern postalPattern;

    public InternationalAddressValidationService() {
        initializeRules();
    }

    public AddressValidationResult validateAddress(String countryCode, Address address) {
        if (countryCode == null) {
            System.out.println("Country code is null");
        }

        if (address == null) {
            System.out.println("Address is null");
        }

        CountryRule rule = rules.get(countryCode);
        if (rule == null) {
            throw new RuntimeException("Country not supported");
        }

        AddressValidationResult result = new AddressValidationResult();

        validateFields(address, rule, result);
        validatePostalCode(address, rule, result);

        return result;
    }

    private void validateFields(Address address, CountryRule rule, AddressValidationResult result) {
        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            result.addViolation("Street must not be empty");
        }

        if (address.getCity() == null || address.getCity().isEmpty()) {
            result.addViolation("City must not be empty");
        }
    }

    private void validatePostalCode(Address address, CountryRule rule, AddressValidationResult result) {
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

    private void initializeRules() {
        rules.put("US", new CountryRule("US", "United States", 5, 5, "\\d{5}", new String[]{"street", "city", "postalCode"}));
        rules.put("CA", new CountryRule("CA", "Canada", 6, 7, "[A-Z]\\d[A-Z] ?\\d[A-Z]\\d", new String[]{"street", "city", "postalCode"}));
        rules.put("DE", new CountryRule("DE", "Germany", 5, 5, "\\d{5}", new String[]{"street", "city", "postalCode"}));
        rules.put("GB", new CountryRule("GB", "United Kingdom", 6, 7, "[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}", new String[]{"street", "city", "postalCode"}));
    }

    public Map<String, CountryRule> getRules() {
        return rules;
    }

    public int getMaxAddressLineLength() {
        return 50;
    }
}

