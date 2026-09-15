package bulk_address_validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Request DTO for single address validation.
 * Replaces the untyped HashMap in the controller.
 */
public class AddressValidationRequest {

    @JsonProperty("country")
    private String countryCode;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("postal")
    private String postalCode;

    public AddressValidationRequest() {
    }

    public AddressValidationRequest(String countryCode, String street, String city, String postalCode) {
        this.countryCode = countryCode;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressValidationRequest that = (AddressValidationRequest) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, street, city, postalCode);
    }

    @Override
    public String toString() {
        return "AddressValidationRequest{" +
                "countryCode='" + countryCode + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

