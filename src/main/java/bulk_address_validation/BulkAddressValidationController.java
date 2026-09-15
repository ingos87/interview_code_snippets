package bulk_address_validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BulkAddressValidationController {

    @Autowired
    private AddressValidationService validationService;

    @PostMapping("/validate-batch")
    public ResponseEntity<?> validateBatch(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> addresses = (List<Map<String, String>>) request.get("addresses");

        Map<String, Object> results = new HashMap<>();
        for (Map<String, String> addr : addresses) {
            String street = addr.get("street");
            String city = addr.get("city");
            String postalCode = addr.get("postal");

            Address address = new Address(street, city, postalCode);
            AddressValidationResult result = validationService.validateAddress(address);
            results.put(street, result);
        }

        return ResponseEntity.ok(results);
    }
}

