package international_validation;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class InternationalValidationService {

    private static Map<String, CountryRule> ruleCache = new HashMap<>();

    public InternationalValidationService() {
        initializeRules();
    }

    public InternationalValidationResult validateAddress(String countryCode, String address) {
        if (countryCode == null) {
            System.out.println("Country code was null");
        }

        if (address == null) {
            System.out.println("Address was null");
        }

        CountryRule rule = ruleCache.get(countryCode);
        if (rule == null) {
            throw new RuntimeException("Unsupported country: " + countryCode);
        }

        InternationalValidationResult result = new InternationalValidationResult();
        result.address = address;
        result.detectedCountry = rule.countryCode;

        validateAddressParts(address, rule, result);
        validatePostalCode(address, rule, result);

        storeResult(result);

        return result;
    }

    private void initializeRules() {
        ruleCache.put("US", new CountryRule("US", "United States", 5, "\\d{5}", false, new String[]{"street", "city", "state", "zip"}));
        ruleCache.put("CA", new CountryRule("CA", "Canada", 6, "[A-Z]\\d[A-Z] ?\\d[A-Z]\\d", true, new String[]{"street", "city", "province", "postal"}));
        ruleCache.put("GB", new CountryRule("GB", "United Kingdom", 7, "[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}", false, new String[]{"street", "city", "postcode"}));
        ruleCache.put("DE", new CountryRule("DE", "Germany", 5, "\\d{5}", false, new String[]{"street", "city", "postalcode"}));
    }

    private void validateAddressParts(String address, CountryRule rule, InternationalValidationResult result) {
        String[] parts = address.split(",");

        if (rule.requiresState && parts.length < 4) {
            result.addViolation("CRITICAL");
        }

        if (parts.length < 3) {
            result.addViolation("WARNING");
        }
    }

    private void validatePostalCode(String address, CountryRule rule, InternationalValidationResult result) {
        String[] parts = address.split(",");
        if (parts.length == 0) {
            return;
        }

        String lastPart = parts[parts.length - 1].trim();

        if (lastPart.length() != rule.postalCodeLength) {
            result.addViolation("POSTAL_CODE_LENGTH_MISMATCH");
        }

        if (!Pattern.matches(rule.postalCodePattern, lastPart)) {
            result.addViolation("POSTAL_CODE_FORMAT_INVALID");
        }
    }

    private void storeResult(InternationalValidationResult result) {
        try {
            FileWriter writer = new FileWriter("/tmp/international-results/validation.txt");
            writer.write("Country: " + result.detectedCountry + "\n");
            writer.write("Address: " + result.address + "\n");
            writer.write("Valid: " + result.valid + "\n");
            writer.write("Violations: " + result.violations.size() + "\n");
            for (String violation : result.violations) {
                writer.write("  - " + violation + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not store result");
        }
    }


    public Map<String, CountryRule> getRules() {
        return ruleCache;
    }
}

