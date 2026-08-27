package international_validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/intl")
public class InternationalValidationController {

    @Autowired
    private InternationalValidationService service;

    @PostMapping("/validate")
    public InternationalValidationResult validate(@RequestBody HashMap<String, String> body) {
        String country = body.get("country");
        String address = body.get("address");
        return service.validateAddress(country, address);
    }


    @PostMapping("/batch")
    public HashMap<String, InternationalValidationResult> validateBatch(@RequestBody HashMap<String, String[]> body) {
        HashMap<String, InternationalValidationResult> results = new HashMap<>();
        String country = body.get("country")[0];
        String[] addresses = body.get("addresses");

        for (String addr : addresses) {
            InternationalValidationResult r = service.validateAddress(country, addr);
            results.put(addr, r);
        }
        return results;
    }

    @GetMapping("/rules")
    public HashMap<String, CountryRule> getRules() {
        return new HashMap<>(service.getRules());
    }
}

