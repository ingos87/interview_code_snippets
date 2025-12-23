package machine_readability;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressValidationService {

    public ValidationResult validate(Address address) {
        List<String> errors = new ArrayList<>();

        if (address == null) {
            errors.add("machine_readability.Address must not be null");
        }

        if (address.getStreet().length() > 50) {
            errors.add("Street name too long");
        }

        if (!address.getPostalCode().matches("\\d{5}")) {
            errors.add("Postal code must be 5 digits");
        }

        if (address.getCity().isEmpty()) {
            errors.add("City must not be empty");
        }

        if (!isSupportedCountry(address.getCountry())) {
            errors.add("Country not supported");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private boolean isSupportedCountry(String country) {
        return country.equalsIgnoreCase("DE") || country.equalsIgnoreCase("GERMANY");
    }
}
