package address_area_layout;

import java.io.IOException;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/visual-validation")
public class AddressAreaValidationController {
    private final AddressAreaValidationService service;

    public AddressAreaValidationController(AddressAreaValidationService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        return service.validatePdf(bytes);
    }
}
