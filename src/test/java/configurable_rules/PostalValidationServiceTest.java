package configurable_rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostalValidationServiceTest {

    @Test
    void testValidation() {
        RuleConfigurationService ruleConfigurationService = new RuleConfigurationService();
        PostalValidationService service = new PostalValidationService(ruleConfigurationService);
        RuleValidationResult result = service.validateAddress("53113", "Musterstraße", "Bonn", "Max Mustermann");
        assertTrue(result.valid);
    }
}

