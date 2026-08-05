package machine_readability;

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
