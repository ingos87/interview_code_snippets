package international_address_validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InternationalAddressValidationController {

    @Autowired
    private InternationalAddressValidationService validationService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateAddress(@RequestBody Map<String, String> request) {
        String countryCode = request.get("country");
        String street = request.get("street");
        String city = request.get("city");
        String postalCode = request.get("postal");
        
        Address address = new Address(street, city, postalCode, countryCode);
        AddressValidationResult result = validationService.validateAddress(countryCode, address);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate-batch")
    public ResponseEntity<?> validateBatch(@RequestBody Map<String, Object> request) {
        String country = (String) request.get("country");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> addresses = (List<Map<String, String>>) request.get("addresses");

        Map<String, Object> results = new HashMap<>();
        for (Map<String, String> addr : addresses) {
            String street = addr.get("street");
            String city = addr.get("city");
            String postalCode = addr.get("postal");

            Address address = new Address(street, city, postalCode, country);
            AddressValidationResult result = validationService.validateAddress(country, address);
            results.put(street, result);
        }

        return ResponseEntity.ok(results);
    }
}

