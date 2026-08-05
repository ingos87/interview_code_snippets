package configurable_rules;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/postal")
public class PostalRuleController {

    private PostalValidationService postalValidationService;
    private RuleConfigurationService ruleConfigurationService;

    public PostalRuleController(PostalValidationService postalValidationService, RuleConfigurationService ruleConfigurationService) {
        this.postalValidationService = postalValidationService;
        this.ruleConfigurationService = ruleConfigurationService;
    }

    @PostMapping("/validate")
    public RuleValidationResult validate(@RequestBody HashMap<String, String> body) {
        String postalCode = body.get("postalCode");
        String street = body.get("street");
        String city = body.get("city");
        String recipient = body.get("recipient");
        return postalValidationService.validateAddress(postalCode, street, city, recipient);
    }

    @GetMapping("/rules")
    public List<PostalRule> getRules() {
        return ruleConfigurationService.getRules();
    }

    @GetMapping("/rules/reload")
    public String reloadRules() {
        ruleConfigurationService.reloadRules();
        return "Rules reloaded";
    }

    @GetMapping("/rules/previous")
    public List<PostalRule> getPreviousRules() {
        return ruleConfigurationService.getPreviousRules();
    }

    @GetMapping("/rules/find")
    public PostalRule findRule(@RequestParam String name) {
        return ruleConfigurationService.getRuleByName(name);
    }
}

