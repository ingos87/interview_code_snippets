package bulk_address_validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import international_address_validation.AddressValidationRequest;

import java.util.List;
import java.util.Objects;

/**
 * Request DTO for batch address validation.
 * Replaces the untyped Map with proper typing.
 */
public class BatchAddressValidationRequest {

    @JsonProperty("addresses")
    private List<AddressValidationRequest> addresses;

    public BatchAddressValidationRequest() {
    }

    public BatchAddressValidationRequest(List<AddressValidationRequest> addresses) {
        this.addresses = addresses;
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
        return Objects.equals(addresses, that.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addresses);
    }

    @Override
    public String toString() {
        return "BatchAddressValidationRequest{" +
                ", addresses=" + addresses +
                '}';
    }
}

