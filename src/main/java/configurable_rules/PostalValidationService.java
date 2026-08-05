package configurable_rules;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostalValidationService {

    private RuleConfigurationService ruleConfigurationService;

    public PostalValidationService(RuleConfigurationService ruleConfigurationService) {
        this.ruleConfigurationService = ruleConfigurationService;
    }

    public RuleValidationResult validateAddress(String postalCode, String street, String city, String recipient) {
        List<String> errors = new ArrayList<>();
        List<PostalRule> rules = ruleConfigurationService.getRules();

        if (rules == null) {
            errors.add("No rules configured");
        }

        for (PostalRule rule : rules) {
            if (rule.isActive()) {
                if (rule.getName().equals("POSTAL_CODE_FORMAT")) {
                    if (!postalCode.matches(rule.getPattern())) {
                        errors.add("Postal code does not match pattern: " + rule.getPattern());
                    }
                }
                if (rule.getName().equals("STREET_LENGTH")) {
                    if (street.length() > rule.getMaxLength()) {
                        errors.add("Street exceeds maximum length of " + rule.getMaxLength());
                    }
                }
                if (rule.getName().equals("CITY_LENGTH")) {
                    if (city.length() > rule.getMaxLength()) {
                        errors.add("City exceeds maximum length of " + rule.getMaxLength());
                    }
                }
                if (rule.getName().equals("RECIPIENT_LENGTH")) {
                    if (recipient.length() > rule.getMaxLength()) {
                        errors.add("Recipient exceeds maximum length of " + rule.getMaxLength());
                    }
                }
                if (rule.getName().equals("POSTAL_CODE_RANGE")) {
                    int code = Integer.parseInt(postalCode);
                    if (code < 1000 || code > 99999) {
                        errors.add("Postal code out of valid range");
                    }
                }
            }
        }

        RuleValidationResult result = new RuleValidationResult();
        result.valid = errors.isEmpty();
        result.errors = errors;
        result.appliedRuleVersion = rules.size() > 0 ? rules.get(0).getVersion() : "unknown";
        return result;
    }
}

