package international_address_validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Request DTO for batch address validation.
 * Replaces the untyped Map with proper typing.
 */
public class BatchAddressValidationRequest {

    @JsonProperty("country")
    private String countryCode;

    @JsonProperty("addresses")
    private List<AddressValidationRequest> addresses;

    public BatchAddressValidationRequest() {
    }

    public BatchAddressValidationRequest(String countryCode, List<AddressValidationRequest> addresses) {
        this.countryCode = countryCode;
        this.addresses = addresses;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<AddressValidationRequest> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressValidationRequest> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchAddressValidationRequest that = (BatchAddressValidationRequest) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(addresses, that.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, addresses);
    }

    @Override
    public String toString() {
        return "BatchAddressValidationRequest{" +
                "countryCode='" + countryCode + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}

