package machine_readability;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/address")
public class AddressValidationController {

    private final AddressValidationService validationService;

    public AddressValidationController(AddressValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestBody Address address) {
        return validationService.validate(address);
    }
}
