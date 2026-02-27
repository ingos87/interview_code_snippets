package address_area_layout;

import java.io.IOException;

@RestController
@RequestMapping("/api/visual-validation")
public class AddressAreaValidationController {
    private final AddressAreaValidationService service;

    public VisualValidationController(AddressAreaValidationService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        return service.validatePdf(bytes);
    }
}
